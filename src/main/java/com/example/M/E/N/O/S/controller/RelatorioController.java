package com.example.M.E.N.O.S.controller;

import com.example.M.E.N.O.S.dto.RelatorioRequest;
import com.example.M.E.N.O.S.dto.RelatorioResponse;
import com.example.M.E.N.O.S.dto.ResultadoEnquete;
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

    // Escola gera um relatório. O corpo é opcional (só o período); os números o servidor calcula.
    @PostMapping("/escola/{escolaId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RelatorioResponse gerar(@PathVariable Long escolaId,
                                   @RequestBody(required = false) RelatorioRequest request) {
        return relatorioService.gerar(escolaId, request);
    }

    @GetMapping("/escola/{escolaId}")
    public List<RelatorioResponse> listarPorEscola(@PathVariable Long escolaId) {
        return relatorioService.listarPorEscola(escolaId);
    }

    @GetMapping("/{id}")
    public RelatorioResponse buscarPorId(@PathVariable Long id) {
        return relatorioService.buscarPorId(id);
    }

    // Use ?status=ativa ou ?status=inativa.
    @GetMapping("/{id}/enquetes")
    public List<ResultadoEnquete> filtrarStatus(@PathVariable Long id, @RequestParam String status) {
        return relatorioService.filtrarStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        relatorioService.deletar(id);
    }
}
