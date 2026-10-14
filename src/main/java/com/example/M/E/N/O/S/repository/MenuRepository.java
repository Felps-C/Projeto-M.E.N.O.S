package com.example.M.E.N.O.S.repository;

import com.example.M.E.N.O.S.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByEscolaIdOrderByDataInicioDesc(Long escolaId);

    List<Menu> findByEscolaIdAndAtivaOrderByDataInicioDesc(Long escolaId, Boolean ativa);
}
