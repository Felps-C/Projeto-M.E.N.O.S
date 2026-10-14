package com.example.M.E.N.O.S.repository;

import com.example.M.E.N.O.S.model.Enquete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnqueteRepository extends JpaRepository<Enquete, Long> {

    List<Enquete> findByEscola_IdOrderByDataInicioDesc(Long escolaId);

    List<Enquete> findByEscola_IdAndAtivaOrderByDataInicioDesc(Long escolaId, Boolean ativa);
}
