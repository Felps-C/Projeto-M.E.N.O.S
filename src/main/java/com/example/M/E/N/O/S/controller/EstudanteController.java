package com.example.M.E.N.O.S.controller;

import com.example.M.E.N.O.S.dto.EstudanteRequest;
import com.example.M.E.N.O.S.model.Estudante;
import com.example.M.E.N.O.S.service.EstudanteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estudantes")
public class EstudanteController {

    private final EstudanteService estudanteService;

    public EstudanteController(EstudanteService estudanteService) {
        this.estudanteService = estudanteService;
    }

    // Use ?escolaId=1 para listar só os estudantes de uma escola.
    @GetMapping
    public List<Estudante> listar(@RequestParam(required = false) Long escolaId) {
        return estudanteService.listar(escolaId);
    }

    @GetMapping("/{id}")
    public Estudante buscarPorId(@PathVariable Long id) {
        return estudanteService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estudante criar(@Valid @RequestBody EstudanteRequest request) {
        return estudanteService.salvar(request);
    }

    @PutMapping("/{id}")
    public Estudante atualizar(@PathVariable Long id, @Valid @RequestBody EstudanteRequest request) {
        return estudanteService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        estudanteService.deletar(id);
    }
}
