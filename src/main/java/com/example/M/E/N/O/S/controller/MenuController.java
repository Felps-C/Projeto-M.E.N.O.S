package com.example.M.E.N.O.S.controller;

import com.example.M.E.N.O.S.dto.MenuRequest;
import com.example.M.E.N.O.S.model.Menu;
import com.example.M.E.N.O.S.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cardapios")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // Escola publica um cardápio
    @PostMapping("/escola/{escolaId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Menu criar(@PathVariable Long escolaId, @Valid @RequestBody MenuRequest request) {
        return menuService.criar(escolaId, request);
    }

    // Lista os cardápios da escola. Use ?ativa=true para ver só os publicados.
    @GetMapping("/escola/{escolaId}")
    public List<Menu> listarPorEscola(@PathVariable Long escolaId,
                                      @RequestParam(required = false) Boolean ativa) {
        return menuService.listarPorEscola(escolaId, ativa);
    }

    @GetMapping("/{id}")
    public Menu buscarPorId(@PathVariable Long id) {
        return menuService.buscarPorId(id);
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
