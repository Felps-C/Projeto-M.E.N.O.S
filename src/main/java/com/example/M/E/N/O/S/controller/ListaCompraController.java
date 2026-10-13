package com.example.M.E.N.O.S.controller;

import com.example.M.E.N.O.S.model.ListaCompra;
import com.example.M.E.N.O.S.service.ListaCompraService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/listas-compras")
public class ListaCompraController {
        private final ListaCompraService listaCompraService;

        public ListaCompraController(ListaCompraService listaCompraService) {
            this.listaCompraService = listaCompraService;
        }

        @GetMapping
        public List<ListaCompra> listar() {
            return listaCompraService.listarTodos();
        }

        @GetMapping("/{id}")
        public ListaCompra buscarPorId(@PathVariable Long id) {
            return listaCompraService.buscarPorId(id);
        }

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public ListaCompra criar(@RequestBody ListaCompra listaCompra) {
            return listaCompraService.salvar(listaCompra);
        }

        @PutMapping("/{id}")
        public ListaCompra atualizar(
                @PathVariable Long id,
                @RequestBody ListaCompra listaCompra) {
            return listaCompraService.atualizar(id, listaCompra);
        }

        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void deletar(@PathVariable Long id) {
            listaCompraService.deletar(id);
        }
    }

