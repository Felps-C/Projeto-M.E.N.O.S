package com.example.M.E.N.O.S.repository;

import com.example.M.E.N.O.S.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsByEstudante_IdAndEnquete_Id(Long estudanteId, Long enqueteId);

    boolean existsByEnquete_Id(Long enqueteId);

    boolean existsByOpcaoEscolhida_Id(Long opcaoId);
}
