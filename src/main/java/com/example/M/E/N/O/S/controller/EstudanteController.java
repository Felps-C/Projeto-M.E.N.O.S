package com.example.M.E.N.O.S.controller;
@RestController
@RequestMapping("/estudantes")
public class EstudanteController {

    private final EstudanteService estudanteService;

    public EstudanteController(EstudanteService estudanteService) {
        this.estudanteService = estudanteService;
    }

    @GetMapping
    public List<Estudante> listar() {
        return estudanteService.listarTodos();
    }

    @GetMapping("/{id}")
    public Estudante buscarPorId(@PathVariable Long id) {
        return estudanteService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estudante criar(@Valid @RequestBody Estudante estudante) {
        return estudanteService.salvar(estudante);
    }

    @PutMapping("/{id}")
    public Estudante atualizar(@PathVariable Long id, @Valid @RequestBody Estudante estudante) {
        return estudanteService.atualizar(id, estudante);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        estudanteService.deletar(id);
    }
}
