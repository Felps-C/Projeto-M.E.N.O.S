package com.example.M.E.N.O.S.repository;

import com.example.M.E.N.O.S.model.Merenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MerendaRepository extends JpaRepository<Merenda, Long> {

    // Pratos de uma escola, em ordem alfabética.
    List<Merenda> findByEscola_IdOrderByNomeAsc(Long escolaId);
}
