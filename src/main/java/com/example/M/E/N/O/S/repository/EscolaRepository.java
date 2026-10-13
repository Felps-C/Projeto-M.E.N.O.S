package com.example.M.E.N.O.S.repository;

import com.example.M.E.N.O.S.model.Escola;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EscolaRepository extends JpaRepository<Escola, Long> {

    Optional<Escola> findByEmail(String email);

    Optional<Escola> findByCnpj(String cnpj);
}
