package com.example.M.E.N.O.S.controller;

import com.example.M.E.N.O.S.dto.ItemListaCompraRequest;
import com.example.M.E.N.O.S.model.ListaCompra;
import com.example.M.E.N.O.S.service.ListaCompraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/listas-compra")
public class ListaCompraController {

    private final ListaCompraService listaCompraService;

    public ListaCompraController(ListaCompraService listaCompraService) {
        this.listaCompraService = listaCompraService;
    }

    // Lista vazia, para montar manualmente
    @PostMapping("/estoque/{estoqueId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ListaCompra criarVazia(@PathVariable Long estoqueId) {
        return listaCompraService.criarVazia(estoqueId);
    }

    // Lista automática a partir do estoque mínimo. ?fator=2 (padrão): repõe até mínimo x 2.
    @PostMapping("/estoque/{estoqueId}/gerar")
    @ResponseStatus(HttpStatus.CREATED)
    public ListaCompra gerarAutomatica(@PathVariable Long estoqueId,
                                       @RequestParam(defaultValue = "2") int fator) {
        return listaCompraService.gerarAutomatica(estoqueId, fator);
    }

    @GetMapping("/estoque/{estoqueId}")
    public List<ListaCompra> listarPorEstoque(@PathVariable Long estoqueId) {
        return listaCompraService.listarPorEstoque(estoqueId);
    }

    @GetMapping("/{id}")
    public ListaCompra buscarPorId(@PathVariable Long id) {
        return listaCompraService.buscarPorId(id);
    }

    @PostMapping("/{id}/itens")
    public ListaCompra adicionarItem(@PathVariable Long id,
                                     @Valid @RequestBody ItemListaCompraRequest request) {
        return listaCompraService.adicionarItem(id, request.produtoId(), request.quantidade());
    }

    @DeleteMapping("/{id}/itens/{produtoId}")
    public ListaCompra removerItem(@PathVariable Long id, @PathVariable Long produtoId) {
        return listaCompraService.removerItem(id, produtoId);
    }

    @PostMapping("/{id}/concluir")
    public ListaCompra concluir(@PathVariable Long id) {
        return listaCompraService.concluir(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        listaCompraService.deletar(id);
    }
}
