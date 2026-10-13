package com.example.M.E.N.O.S.service;

import com.example.M.E.N.O.S.model.ListaCompra;
import com.example.M.E.N.O.S.repository.ListaCompraRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ListaCompraService {

        private final ListaCompraRepository listaCompraRepository;

        public ListaCompraService(ListaCompraRepository listaCompraRepository) {
            this.listaCompraRepository = listaCompraRepository;
        }

        public List<ListaCompra> listarTodos() {
            return listaCompraRepository.findAll();
        }

        public ListaCompra buscarPorId(Long id) {
            return listaCompraRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Lista de compra não encontrada com id: " + id
                    ));
        }

        public ListaCompra salvar(ListaCompra listaCompra) {
            listaCompra.setId(null);
            return listaCompraRepository.save(listaCompra);
        }

        public ListaCompra atualizar(Long id, ListaCompra dadosAtualizados) {
            ListaCompra listaCompra = buscarPorId(id);

            listaCompra.setStatus(dadosAtualizados.getStatus());
            listaCompra.setDataCriacao(dadosAtualizados.getDataCriacao());
            listaCompra.setItens(dadosAtualizados.getItens());
            listaCompra.setEstoque(dadosAtualizados.getEstoque());

            return listaCompraRepository.save(listaCompra);
        }

        public void deletar(Long id) {
            ListaCompra listaCompra = buscarPorId(id);
            listaCompraRepository.delete(listaCompra);
        }
    }


