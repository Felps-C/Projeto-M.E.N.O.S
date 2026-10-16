package com.example.M.E.N.O.S.service;

import com.example.M.E.N.O.S.dto.AlertasEstoque;
import com.example.M.E.N.O.S.model.Escola;
import com.example.M.E.N.O.S.model.Estoque;
import com.example.M.E.N.O.S.model.Movimentacao;
import com.example.M.E.N.O.S.model.Produto;
import com.example.M.E.N.O.S.model.TipoMovimentacao;
import com.example.M.E.N.O.S.repository.EscolaRepository;
import com.example.M.E.N.O.S.repository.EstoqueRepository;
import com.example.M.E.N.O.S.repository.MovimentacaoRepository;
import com.example.M.E.N.O.S.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final EscolaRepository escolaRepository;
    private final ProdutoRepository produtoRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    // Injeção de dependência via construtor
    public EstoqueService(EstoqueRepository estoqueRepository,
                          EscolaRepository escolaRepository,
                          ProdutoRepository produtoRepository,
                          MovimentacaoRepository movimentacaoRepository) {
        this.estoqueRepository = estoqueRepository;
        this.escolaRepository = escolaRepository;
        this.produtoRepository = produtoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    // ---------- Estoque ----------

    @Transactional
    public Estoque criarParaEscola(Long escolaId) {
        Escola escola = escolaRepository.findById(escolaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Escola não encontrada com id: " + escolaId));
        if (estoqueRepository.existsByEscola_Id(escolaId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta escola já possui um estoque");
        }
        return estoqueRepository.save(new Estoque(escola));
    }

    @Transactional(readOnly = true)
    public Estoque buscarPorId(Long id) {
        return estoqueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Estoque não encontrado com id: " + id));
    }

    @Transactional(readOnly = true)
    public Estoque buscarPorEscola(Long escolaId) {
        return estoqueRepository.findByEscola_Id(escolaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "A escola " + escolaId + " ainda não possui estoque"));
    }

    // ---------- Produtos do estoque (RF03) ----------

    @Transactional(readOnly = true)
    public List<Produto> listarProdutos(Long estoqueId) {
        return List.copyOf(buscarPorId(estoqueId).getProdutos());
    }

    // Cria o produto e já o coloca dentro do estoque.
    @Transactional
    public Produto adicionarProduto(Long estoqueId, Produto produto) {
        Estoque estoque = buscarPorId(estoqueId);
        produto.setId(null);
        Produto salvo = produtoRepository.save(produto);
        estoque.getProdutos().add(salvo);
        return salvo;
    }

    // ---------- Entradas e saídas (RF07) ----------

    @Transactional
    public Movimentacao registrarEntrada(Long estoqueId, Long produtoId, int quantidade) {
        return movimentar(estoqueId, produtoId, quantidade, TipoMovimentacao.ENTRADA);
    }

    @Transactional
    public Movimentacao registrarSaida(Long estoqueId, Long produtoId, int quantidade) {
        return movimentar(estoqueId, produtoId, quantidade, TipoMovimentacao.SAIDA);
    }

    private Movimentacao movimentar(Long estoqueId, Long produtoId, int quantidade, TipoMovimentacao tipo) {
        if (quantidade <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A quantidade deve ser maior que zero");
        }
        // Trava o produto ANTES de ler o saldo. Assim duas saídas simultâneas não passam juntas
        // pela checagem "tem saldo?" (a segunda espera a primeira terminar).
        Produto produto = produtoRepository.buscarParaAtualizar(produtoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produto não encontrado com id: " + produtoId));
        Estoque estoque = buscarPorId(estoqueId);
        produtoDoEstoque(estoque, produtoId); // confere que o produto é deste estoque

        if (tipo == TipoMovimentacao.SAIDA) {
            if (produto.getQuantidade() < quantidade) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Quantidade insuficiente em estoque (disponível: " + produto.getQuantidade() + ")");
            }
            produto.setQuantidade(produto.getQuantidade() - quantidade);
        } else {
            produto.setQuantidade(produto.getQuantidade() + quantidade);
        }
        // O produto está gerenciado pela transação, então a nova quantidade é salva sozinha.
        return movimentacaoRepository.save(new Movimentacao(produto, tipo, quantidade));
    }

    @Transactional(readOnly = true)
    public List<Movimentacao> listarMovimentacoes(Long estoqueId) {
        List<Long> ids = buscarPorId(estoqueId).getProdutos().stream().map(Produto::getId).toList();
        if (ids.isEmpty()) {
            return List.of();
        }
        return movimentacaoRepository.findByProdutoIdInOrderByDataHoraDesc(ids);
    }

    // ---------- Alertas (RF08) ----------

    @Transactional(readOnly = true)
    public AlertasEstoque gerarAlertas(Long estoqueId, int dias) {
        if (dias < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O número de dias não pode ser negativo");
        }
        Estoque estoque = buscarPorId(estoqueId);
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(dias);

        List<Produto> vencidos = estoque.getProdutos().stream()
                .filter(p -> p.getQuantidade() > 0 && p.getValidade() != null && p.getValidade().isBefore(hoje))
                .toList();

        List<Produto> aVencer = estoque.getProdutos().stream()
                .filter(p -> p.getQuantidade() > 0 && p.getValidade() != null
                        && !p.getValidade().isBefore(hoje) && !p.getValidade().isAfter(limite))
                .toList();

        List<Produto> abaixoDoMinimo = estoque.getProdutos().stream()
                .filter(p -> p.getEstoqueMinimo() > 0 && p.getQuantidade() <= p.getEstoqueMinimo())
                .toList();

        return new AlertasEstoque(dias, vencidos, aVencer, abaixoDoMinimo);
    }

    private Produto produtoDoEstoque(Estoque estoque, Long produtoId) {
        return estoque.getProdutos().stream()
                .filter(p -> p.getId().equals(produtoId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto " + produtoId + " não pertence ao estoque " + estoque.getId()));
    }
}
