package com.example.M.E.N.O.S.repository;

import com.example.M.E.N.O.S.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsByEstudanteIdAndEnqueteId(Long estudanteId, Long enqueteId);

    boolean existsByEnqueteId(Long enqueteId);

    boolean existsByOpcaoEscolhidaId(Long opcaoId);
}
