package com.example.M.E.N.O.S.repository;

import com.example.M.E.N.O.S.model.ListaCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListaCompraRepository extends JpaRepository<ListaCompra, Long> {

    List<ListaCompra> findByEstoque_IdOrderByDataCriacaoDesc(Long estoqueId);
}
