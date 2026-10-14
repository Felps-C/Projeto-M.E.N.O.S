package com.example.M.E.N.O.S.controller;

import com.example.M.E.N.O.S.dto.AlertasEstoque;
import com.example.M.E.N.O.S.dto.MovimentacaoRequest;
import com.example.M.E.N.O.S.model.Estoque;
import com.example.M.E.N.O.S.model.Movimentacao;
import com.example.M.E.N.O.S.model.Produto;
import com.example.M.E.N.O.S.service.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoques")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    // Cria o estoque de uma escola (cada escola tem um só)
    @PostMapping("/escola/{escolaId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Estoque criarParaEscola(@PathVariable Long escolaId) {
        return estoqueService.criarParaEscola(escolaId);
    }

    @GetMapping("/{id}")
    public Estoque buscarPorId(@PathVariable Long id) {
        return estoqueService.buscarPorId(id);
    }

    @GetMapping("/escola/{escolaId}")
    public Estoque buscarPorEscola(@PathVariable Long escolaId) {
        return estoqueService.buscarPorEscola(escolaId);
    }

    @GetMapping("/{id}/produtos")
    public List<Produto> listarProdutos(@PathVariable Long id) {
        return estoqueService.listarProdutos(id);
    }

    // Cadastra um produto já dentro do estoque
    @PostMapping("/{id}/produtos")
    @ResponseStatus(HttpStatus.CREATED)
    public Produto adicionarProduto(@PathVariable Long id, @Valid @RequestBody Produto produto) {
        return estoqueService.adicionarProduto(id, produto);
    }

    @PostMapping("/{id}/produtos/{produtoId}/entrada")
    @ResponseStatus(HttpStatus.CREATED)
    public Movimentacao registrarEntrada(@PathVariable Long id,
                                         @PathVariable Long produtoId,
                                         @Valid @RequestBody MovimentacaoRequest request) {
        return estoqueService.registrarEntrada(id, produtoId, request.quantidade());
    }

    @PostMapping("/{id}/produtos/{produtoId}/saida")
    @ResponseStatus(HttpStatus.CREATED)
    public Movimentacao registrarSaida(@PathVariable Long id,
                                       @PathVariable Long produtoId,
                                       @Valid @RequestBody MovimentacaoRequest request) {
        return estoqueService.registrarSaida(id, produtoId, request.quantidade());
    }

    @GetMapping("/{id}/movimentacoes")
    public List<Movimentacao> listarMovimentacoes(@PathVariable Long id) {
        return estoqueService.listarMovimentacoes(id);
    }

    // Alertas de validade e estoque mínimo. "dias" = janela para considerar "a vencer".
    @GetMapping("/{id}/alertas")
    public AlertasEstoque alertas(@PathVariable Long id,
                                  @RequestParam(defaultValue = "7") int dias) {
        return estoqueService.gerarAlertas(id, dias);
    }
}
