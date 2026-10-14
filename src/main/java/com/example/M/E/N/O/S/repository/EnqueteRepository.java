package com.example.M.E.N.O.S.repository;

import com.example.M.E.N.O.S.model.Enquete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnqueteRepository extends JpaRepository<Enquete, Long> {

    List<Enquete> findByEscolaIdOrderByDataInicioDesc(Long escolaId);

    List<Enquete> findByEscolaIdAndAtivaOrderByDataInicioDesc(Long escolaId, Boolean ativa);
}
