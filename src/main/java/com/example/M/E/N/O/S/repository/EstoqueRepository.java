package com.example.M.E.N.O.S.repository;

import com.example.M.E.N.O.S.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    Optional<Estoque> findByEscola_Id(Long escolaId);

    boolean existsByEscola_Id(Long escolaId);
}
