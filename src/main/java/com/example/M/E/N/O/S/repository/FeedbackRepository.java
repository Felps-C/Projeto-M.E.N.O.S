package com.example.M.E.N.O.S.repository;

import com.example.M.E.N.O.S.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findAllByOrderByDataDescIdDesc();

    // O "_" diz ao Spring Data para entrar na relação (menu -> id).
    List<Feedback> findByMenu_IdOrderByDataDescIdDesc(Long menuId);

    // menu -> escola -> id: feedbacks de todos os cardápios de uma escola.
    List<Feedback> findByMenu_Escola_IdOrderByDataDescIdDesc(Long escolaId);
}
