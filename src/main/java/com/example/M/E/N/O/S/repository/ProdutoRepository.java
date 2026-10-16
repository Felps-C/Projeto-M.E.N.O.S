package com.example.M.E.N.O.S.repository;

import com.example.M.E.N.O.S.model.Produto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Busca o produto TRAVANDO a linha no banco até o fim da transação.
    // Usado nas entradas/saídas: duas saídas ao mesmo tempo ficam em fila e não furam a checagem de saldo.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Produto p where p.id = :id")
    Optional<Produto> buscarParaAtualizar(@Param("id") Long id);
}
