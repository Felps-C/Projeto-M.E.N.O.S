package com.example.M.E.N.O.S.service;

import com.example.M.E.N.O.S.dto.VotoRequest;
import com.example.M.E.N.O.S.model.Enquete;
import com.example.M.E.N.O.S.model.Estudante;
import com.example.M.E.N.O.S.model.OpcaoVoto;
import com.example.M.E.N.O.S.model.Voto;
import com.example.M.E.N.O.S.repository.EnqueteRepository;
import com.example.M.E.N.O.S.repository.EstudanteRepository;
import com.example.M.E.N.O.S.repository.VotoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final EnqueteRepository enqueteRepository;
    private final EstudanteRepository estudanteRepository;

    // Injeção de dependência via construtor
    public VotoService(VotoRepository votoRepository,
                       EnqueteRepository enqueteRepository,
                       EstudanteRepository estudanteRepository) {
        this.votoRepository = votoRepository;
        this.enqueteRepository = enqueteRepository;
        this.estudanteRepository = estudanteRepository;
    }

    // Regras: enquete aberta, estudante da mesma escola da enquete,
    // opção pertencente à enquete e um único voto por estudante em cada enquete.
    @Transactional
    public Voto votar(Long enqueteId, VotoRequest request) {
        Enquete enquete = enqueteRepository.findById(enqueteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Enquete não encontrada com id: " + enqueteId));
        Estudante estudante = estudanteRepository.findById(request.estudanteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Estudante não encontrado com id: " + request.estudanteId()));

        if (!enquete.isAberta()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta enquete não está aberta para votação");
        }
        if (!estudante.getEscola().getId().equals(enquete.getEscola().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "O estudante não pertence à escola desta enquete");
        }

        OpcaoVoto opcao = enquete.getOpcoes().stream()
                .filter(o -> o.getId().equals(request.opcaoId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "A opção " + request.opcaoId() + " não pertence à enquete " + enqueteId));

        if (votoRepository.existsByEstudanteIdAndEnqueteId(estudante.getId(), enqueteId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este estudante já votou nesta enquete");
        }

        Voto voto = new Voto(null, estudante, opcao, LocalDateTime.now());
        voto.setEnquete(enquete);
        try {
            return votoRepository.saveAndFlush(voto);
        } catch (DataIntegrityViolationException e) {
            // Dois votos simultâneos do mesmo estudante: o banco barra o segundo.
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este estudante já votou nesta enquete");
        }
    }
}
