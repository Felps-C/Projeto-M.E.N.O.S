package com.example.M.E.N.O.S.controller;

import com.example.M.E.N.O.S.dto.MenuRequest;
import com.example.M.E.N.O.S.model.Menu;
import com.example.M.E.N.O.S.service.MenuService;
import com.example.M.E.N.O.S.dto.ResumoCardapio;
import com.example.M.E.N.O.S.service.ResumoCardapioService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cardapios")
public class MenuController {

    private final MenuService menuService;
    private final ResumoCardapioService resumoCardapioService;

    public MenuController(MenuService menuService, ResumoCardapioService resumoCardapioService) {
        this.menuService = menuService;
        this.resumoCardapioService = resumoCardapioService;
    }

    // Escola publica um cardápio
    @PostMapping("/escola/{escolaId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Menu criar(@PathVariable Long escolaId, @Valid @RequestBody MenuRequest request) {
        return menuService.criar(escolaId, request);
    }

    // Lista os cardápios da escola. Use ?ativa=true para ver só os publicados.
    // Também aceita ?data=2026-10-14 para ver só os cardápios daquele dia.
    @GetMapping("/escola/{escolaId}")
    public List<Menu> listarPorEscola(@PathVariable Long escolaId,
                                      @RequestParam(required = false) Boolean ativa,
                                      @RequestParam(required = false)
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return menuService.listarPorEscola(escolaId, ativa, data);
    }

    @GetMapping("/{id}")
    public Menu buscarPorId(@PathVariable Long id) {
        return menuService.buscarPorId(id);
    }

    // Dados do card do aluno: total de curtidas, se este aluno já curtiu e total de comentários.
    // Use ?estudanteId=1 para saber se esse aluno já curtiu.
    @GetMapping("/{id}/resumo")
    public ResumoCardapio resumo(@PathVariable Long id, @RequestParam(required = false) Long estudanteId) {
        return resumoCardapioService.resumir(id, estudanteId);
    }

    @PutMapping("/{id}")
    public Menu atualizar(@PathVariable Long id, @Valid @RequestBody MenuRequest request) {
        return menuService.atualizar(id, request);
    }

    @PostMapping("/{id}/merendas/{merendaId}")
    public Menu adicionarMerenda(@PathVariable Long id, @PathVariable Long merendaId) {
        return menuService.adicionarMerenda(id, merendaId);
    }

    @DeleteMapping("/{id}/merendas/{merendaId}")
    public Menu removerMerenda(@PathVariable Long id, @PathVariable Long merendaId) {
        return menuService.removerMerenda(id, merendaId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        menuService.deletar(id);
    }
}
