package com.example.M.E.N.O.S.service;

import com.example.M.E.N.O.S.model.Estoque;
import com.example.M.E.N.O.S.model.ListaCompra;
import com.example.M.E.N.O.S.model.Produto;
import com.example.M.E.N.O.S.repository.EstoqueRepository;
import com.example.M.E.N.O.S.repository.ListaCompraRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ListaCompraService {

    private final ListaCompraRepository listaCompraRepository;
    private final EstoqueRepository estoqueRepository;

    // Injeção de dependência via construtor
    public ListaCompraService(ListaCompraRepository listaCompraRepository,
                              EstoqueRepository estoqueRepository) {
        this.listaCompraRepository = listaCompraRepository;
        this.estoqueRepository = estoqueRepository;
    }

    // Cria uma lista vazia, para o usuário ir adicionando itens manualmente.
    @Transactional
    public ListaCompra criarVazia(Long estoqueId) {
        Estoque estoque = buscarEstoque(estoqueId);
        return listaCompraRepository.save(new ListaCompra(estoque));
    }

    // Lista automática (RF16): inclui os produtos que estão no estoque mínimo ou abaixo.
    // "fator" define até quanto repor: estoqueMinimo x fator (padrão 2).
    @Transactional
    public ListaCompra gerarAutomatica(Long estoqueId, int fator) {
        if (fator < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O fator deve ser pelo menos 1");
        }
        Estoque estoque = buscarEstoque(estoqueId);
        ListaCompra lista = new ListaCompra(estoque);
        lista.gerarLista(estoque.getProdutos(), fator);
        if (lista.getItens().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Nenhum produto está no estoque mínimo ou abaixo dele, não há o que comprar");
        }
        return listaCompraRepository.save(lista);
    }

    @Transactional(readOnly = true)
    public List<ListaCompra> listarPorEstoque(Long estoqueId) {
        buscarEstoque(estoqueId); // garante o 404 se o estoque não existir
        return listaCompraRepository.findByEstoque_IdOrderByDataCriacaoDesc(estoqueId);
    }

    @Transactional(readOnly = true)
    public ListaCompra buscarPorId(Long id) {
        return listaCompraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Lista de compras não encontrada com id: " + id));
    }

    // Se o produto já está na lista, soma a quantidade.
    @Transactional
    public ListaCompra adicionarItem(Long listaId, Long produtoId, int quantidade) {
        if (quantidade <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A quantidade deve ser maior que zero");
        }
        ListaCompra lista = buscarPorId(listaId);
        garantirAberta(lista);
        Produto produto = lista.getEstoque().getProdutos().stream()
                .filter(p -> p.getId().equals(produtoId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto " + produtoId + " não pertence ao estoque desta lista"));
        lista.adicionarItem(produto, quantidade);
        return listaCompraRepository.saveAndFlush(lista);
    }

    @Transactional
    public ListaCompra removerItem(Long listaId, Long produtoId) {
        ListaCompra lista = buscarPorId(listaId);
        garantirAberta(lista);
        boolean existe = lista.getItens().stream().anyMatch(i -> i.getProduto().getId().equals(produtoId));
        if (!existe) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "O produto " + produtoId + " não está nesta lista");
        }
        lista.removerItem(produtoId);
        return listaCompraRepository.saveAndFlush(lista);
    }

    // Marca a lista como concluída (compra feita). Depois disso ela não pode mais ser editada.
    @Transactional
    public ListaCompra concluir(Long listaId) {
        ListaCompra lista = buscarPorId(listaId);
        lista.setStatus(ListaCompra.CONCLUIDA);
        return listaCompraRepository.save(lista);
    }

    @Transactional
    public void deletar(Long listaId) {
        listaCompraRepository.delete(buscarPorId(listaId));
    }

    // ---------- auxiliares ----------

    private Estoque buscarEstoque(Long estoqueId) {
        return estoqueRepository.findById(estoqueId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Estoque não encontrado com id: " + estoqueId));
    }

    private void garantirAberta(ListaCompra lista) {
        if (!ListaCompra.ABERTA.equals(lista.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Esta lista já foi concluída e não pode ser alterada");
        }
    }
}
