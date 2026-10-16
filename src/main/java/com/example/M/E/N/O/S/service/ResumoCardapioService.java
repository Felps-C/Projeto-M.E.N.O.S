package com.example.M.E.N.O.S.service;

import com.example.M.E.N.O.S.dto.ResumoCardapio;
import com.example.M.E.N.O.S.repository.CurtidaRepository;
import com.example.M.E.N.O.S.repository.EstudanteRepository;
import com.example.M.E.N.O.S.repository.FeedbackRepository;
import com.example.M.E.N.O.S.repository.MenuRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ResumoCardapioService {

    private final MenuRepository menuRepository;
    private final CurtidaRepository curtidaRepository;
    private final FeedbackRepository feedbackRepository;
    private final EstudanteRepository estudanteRepository;

    public ResumoCardapioService(MenuRepository menuRepository,
                                 CurtidaRepository curtidaRepository,
                                 FeedbackRepository feedbackRepository,
                                 EstudanteRepository estudanteRepository) {
        this.menuRepository = menuRepository;
        this.curtidaRepository = curtidaRepository;
        this.feedbackRepository = feedbackRepository;
        this.estudanteRepository = estudanteRepository;
    }

    @Transactional(readOnly = true)
    public ResumoCardapio resumir(Long menuId, Long estudanteId) {
        if (!menuRepository.existsById(menuId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cardápio não encontrado com id: " + menuId);
        }
        Boolean curtiu = null;
        if (estudanteId != null) {
            if (!estudanteRepository.existsById(estudanteId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudante não encontrado com id: " + estudanteId);
            }
            curtiu = curtidaRepository.existsByEstudante_IdAndMenu_Id(estudanteId, menuId);
        }
        return new ResumoCardapio(menuId,
                curtidaRepository.countByMenu_Id(menuId),
                curtiu,
                feedbackRepository.countByMenu_Id(menuId));
    }
}
