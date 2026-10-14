package com.example.M.E.N.O.S.controller;

import com.example.M.E.N.O.S.model.Enquete;
import com.example.M.E.N.O.S.model.Relatorio;
import com.example.M.E.N.O.S.service.RelatorioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping
    public List<Relatorio> listar() {
        return relatorioService.listarTodos();
    }

    @GetMapping("/{id}")
    public Relatorio buscarPorId(@PathVariable Long id) {
        return relatorioService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Relatorio criar(@RequestBody Relatorio relatorio) {
        return relatorioService.salvar(relatorio);
    }

    @PutMapping("/{id}")
    public Relatorio atualizar(
            @PathVariable Long id,
            @RequestBody Relatorio relatorio) {

        return relatorioService.atualizar(id, relatorio);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        relatorioService.deletar(id);
    }

    @PostMapping("/compilar")
    @ResponseStatus(HttpStatus.CREATED)
    public Relatorio compilarDados(
            @RequestBody List<Enquete> enquetes) {

        return relatorioService.compilarDados(enquetes);
    }

    @GetMapping("/{id}/enquetes")
    public List<Enquete> filtrarStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return relatorioService.filtrarStatus(id, status);
    }
}
