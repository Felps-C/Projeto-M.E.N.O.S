package com.example.M.E.N.O.S.service;

import com.example.M.E.N.O.S.model.Produto;
import com.example.M.E.N.O.S.repository.ProdutoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    // Injeção de dependência via construtor
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produto não encontrado com id: " + id));
    }

    public Produto salvar(Produto produto) {
        produto.setId(null); // garante que o POST sempre cria um registro novo
        return produtoRepository.save(produto);
    }

    public Produto atualizar(Long id, Produto dadosAtualizados) {
        Produto produto = buscarPorId(id);
        produto.setNome(dadosAtualizados.getNome());
        produto.setValidade(dadosAtualizados.getValidade());
        produto.setEstoqueMinimo(dadosAtualizados.getEstoqueMinimo());
        produto.setCategoria(dadosAtualizados.getCategoria());
        produto.setUnidade(dadosAtualizados.getUnidade());
        // A quantidade NÃO é alterada aqui, de propósito: ela só muda por entrada/saída
        // (POST /estoques/{id}/produtos/{produtoId}/entrada|saida), assim o histórico de
        // movimentações sempre bate com o saldo.
        return produtoRepository.save(produto);
    }

    @Transactional
    public void deletar(Long id) {
        Produto produto = buscarPorId(id);
        try {
            produtoRepository.delete(produto);
            produtoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "O produto está em uso (lista de compras ou histórico de movimentação) e não pode ser excluído");
        }
    }
}
