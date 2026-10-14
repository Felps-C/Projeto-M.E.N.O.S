package com.example.M.E.N.O.S.service;

import com.example.M.E.N.O.S.dto.FeedbackRequest;
import com.example.M.E.N.O.S.model.Estudante;
import com.example.M.E.N.O.S.model.Feedback;
import com.example.M.E.N.O.S.model.Menu;
import com.example.M.E.N.O.S.repository.EscolaRepository;
import com.example.M.E.N.O.S.repository.EstudanteRepository;
import com.example.M.E.N.O.S.repository.FeedbackRepository;
import com.example.M.E.N.O.S.repository.MenuRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final EstudanteRepository estudanteRepository;
    private final MenuRepository menuRepository;
    private final EscolaRepository escolaRepository;

    // Injeção de dependência via construtor
    public FeedbackService(FeedbackRepository feedbackRepository,
                           EstudanteRepository estudanteRepository,
                           MenuRepository menuRepository,
                           EscolaRepository escolaRepository) {
        this.feedbackRepository = feedbackRepository;
        this.estudanteRepository = estudanteRepository;
        this.menuRepository = menuRepository;
        this.escolaRepository = escolaRepository;
    }

    // Sem filtro lista todos; com menuId, os de um cardápio; com escolaId, os de uma escola.
    @Transactional(readOnly = true)
    public List<Feedback> listar(Long menuId, Long escolaId) {
        if (menuId != null) {
            if (!menuRepository.existsById(menuId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cardápio não encontrado com id: " + menuId);
            }
            return feedbackRepository.findByMenu_IdOrderByDataDescIdDesc(menuId);
        }
        if (escolaId != null) {
            if (!escolaRepository.existsById(escolaId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Escola não encontrada com id: " + escolaId);
            }
            return feedbackRepository.findByMenu_Escola_IdOrderByDataDescIdDesc(escolaId);
        }
        return feedbackRepository.findAllByOrderByDataDescIdDesc();
    }

    @Transactional(readOnly = true)
    public Feedback buscarPorId(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Feedback não encontrado com id: " + id));
    }

    // O estudante só pode comentar cardápios da própria escola. A data é gerada aqui.
    @Transactional
    public Feedback criar(FeedbackRequest request) {
        if (request.estudanteId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O estudante é obrigatório");
        }
        if (request.menuId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O cardápio é obrigatório");
        }
        Estudante estudante = estudanteRepository.findById(request.estudanteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Estudante não encontrado com id: " + request.estudanteId()));
        Menu menu = menuRepository.findById(request.menuId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cardápio não encontrado com id: " + request.menuId()));

        if (!estudante.getEscola().getId().equals(menu.getEscola().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "O estudante não pertence à escola deste cardápio");
        }

        Feedback feedback = new Feedback();
        feedback.setTexto(request.texto().trim());
        feedback.setData(LocalDate.now());
        feedback.setEstudante(estudante);
        feedback.setMenu(menu);
        return feedbackRepository.save(feedback);
    }

    // Só o texto pode ser editado.
    @Transactional
    public Feedback atualizar(Long id, FeedbackRequest request) {
        Feedback feedback = buscarPorId(id);
        feedback.setTexto(request.texto().trim());
        return feedbackRepository.save(feedback);
    }

    @Transactional
    public void deletar(Long id) {
        feedbackRepository.delete(buscarPorId(id));
    }
}
