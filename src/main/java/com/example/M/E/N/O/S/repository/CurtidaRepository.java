package com.example.M.E.N.O.S.repository;

import com.example.M.E.N.O.S.model.Curtida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurtidaRepository extends JpaRepository<Curtida, Long> {

    boolean existsByEstudante_IdAndMenu_Id(Long estudanteId, Long menuId);

    Optional<Curtida> findByEstudante_IdAndMenu_Id(Long estudanteId, Long menuId);

    long countByMenu_Id(Long menuId);
}
