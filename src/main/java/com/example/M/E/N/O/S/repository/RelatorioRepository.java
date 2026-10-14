package com.example.M.E.N.O.S.repository;

import com.example.M.E.N.O.S.model.Relatorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {

    List<Relatorio> findByEscola_IdOrderByDataGeracaoDesc(Long escolaId);
}
