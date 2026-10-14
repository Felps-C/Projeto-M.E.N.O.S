package com.example.M.E.N.O.S.repository;

import com.example.M.E.N.O.S.model.Estudante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstudanteRepository extends JpaRepository<Estudante, Long> {

    Optional<Estudante> findByEmail(String email);

    // A matrícula só precisa ser única dentro da mesma escola.
    Optional<Estudante> findByEscola_IdAndMatriculaEscolar(Long escolaId, String matriculaEscolar);

    List<Estudante> findByEscola_Id(Long escolaId);
}
