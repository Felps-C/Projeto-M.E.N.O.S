package com.example.M.E.N.O.S.controller;

import com.example.M.E.N.O.S.model.Feedback;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.M.E.N.O.S.repository.FeedbackRepository;


import java.util.List;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    public final FeedbackRepository feedbackRepository;

    public FeedbackController(FeedbackRepository feedbackRepository){
        this.feedbackRepository = feedbackRepository;
    }

    @GetMapping
    public List<Feedback> listar(){
        return feedbackRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> buscarPorId(
            @PathVariable Long id) {

        return feedbackRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Feedback> cadastrar(
            @RequestBody Feedback feedback) {

        Feedback novoFeedback =
                feedbackRepository.save(feedback);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(novoFeedback);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Feedback> editar(
            @PathVariable Long id,
            @RequestBody Feedback feedback) {

        return feedbackRepository.findById(id)
                .map(feedbackExistente -> {
                    feedbackExistente.setTexto(feedback.getTexto());
                    feedbackExistente.setData(feedback.getData());
                    feedbackExistente.setEstudante(feedback.getEstudante());
                    feedbackExistente.setMenu(feedback.getMenu());

                    Feedback atualizado =
                            feedbackRepository.save(feedbackExistente);

                    return ResponseEntity.ok(atualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        if (!feedbackRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        feedbackRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}

