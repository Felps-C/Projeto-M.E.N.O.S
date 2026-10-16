package com.example.M.E.N.O.S.repository;

import com.example.M.E.N.O.S.model.Escola;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EscolaRepository extends JpaRepository<Escola, Long> {

    Optional<Escola> findByEmail(String email);

    Optional<Escola> findByCnpj(String cnpj);

    // Versões em lista, usadas no login: o banco pode ter escolas antigas com o mesmo CNPJ.
    List<Escola> findAllByCnpj(String cnpj);

    // Acha a escola pelo CNPJ mesmo que esteja guardado com pontos, barra e traço.
    // Recebe o CNPJ já sem pontuação e em maiúsculas.
    @Query("select e from Escola e where upper(replace(replace(replace(replace(e.cnpj, '.', ''), '-', ''), '/', ''), ' ', '')) = :cnpj")
    Optional<Escola> findByCnpjSemPontuacao(@Param("cnpj") String cnpj);

    @Query("select e from Escola e where upper(replace(replace(replace(replace(e.cnpj, '.', ''), '-', ''), '/', ''), ' ', '')) = :cnpj")
    List<Escola> findAllByCnpjSemPontuacao(@Param("cnpj") String cnpj);
}
