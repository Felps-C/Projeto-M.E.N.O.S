package com.example.M.E.N.O.S.service;

import com.example.M.E.N.O.S.dto.EstudanteRequest;
import com.example.M.E.N.O.S.model.Escola;
import com.example.M.E.N.O.S.model.Estudante;
import com.example.M.E.N.O.S.repository.EscolaRepository;
import com.example.M.E.N.O.S.repository.EstudanteRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EstudanteService {

    private final EstudanteRepository estudanteRepository;
    private final EscolaRepository escolaRepository;
    private final SenhaService senhaService;

    // Injeção de dependência via construtor (forma recomendada no Spring)
    public EstudanteService(EstudanteRepository estudanteRepository,
                            EscolaRepository escolaRepository,
                            SenhaService senhaService) {
        this.estudanteRepository = estudanteRepository;
        this.escolaRepository = escolaRepository;
        this.senhaService = senhaService;
    }

    // Sem filtro lista todos; com escolaId lista só os estudantes daquela escola.
    public List<Estudante> listar(Long escolaId) {
        if (escolaId == null) {
            return estudanteRepository.findAll();
        }
        if (!escolaRepository.existsById(escolaId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Escola não encontrada com id: " + escolaId);
        }
        return estudanteRepository.findByEscola_Id(escolaId);
    }

    public Estudante buscarPorId(Long id) {
        return estudanteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Estudante não encontrado com id: " + id));
    }

    @Transactional
    public Estudante salvar(EstudanteRequest request) {
        if (request.senha() == null || request.senha().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha é obrigatória");
        }
        Escola escola = buscarEscola(request.escolaId());

        estudanteRepository.findByEmail(request.email()).ifPresent(outro -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um estudante com este e-mail");
        });
        estudanteRepository.findByEscola_IdAndMatriculaEscolar(escola.getId(), request.matriculaEscolar())
                .ifPresent(outro -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Já existe um estudante com esta matrícula nesta escola");
                });

        Estudante estudante = new Estudante();
        estudante.setNome(request.nome());
        estudante.setEmail(request.email());
        estudante.setSenha(senhaService.gerarHash(request.senha()));
        estudante.setMatriculaEscolar(request.matriculaEscolar());
        estudante.setAlergias(request.alergias());
        estudante.setEscola(escola);
        return estudanteRepository.save(estudante);
    }

    @Transactional
    public Estudante atualizar(Long id, EstudanteRequest request) {
        Estudante estudante = buscarPorId(id);
        Escola escola = buscarEscola(request.escolaId());

        // O e-mail e a matrícula não podem ser de OUTRO estudante.
        estudanteRepository.findByEmail(request.email())
                .filter(outro -> !outro.getId().equals(id))
                .ifPresent(outro -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um estudante com este e-mail");
                });
        estudanteRepository.findByEscola_IdAndMatriculaEscolar(escola.getId(), request.matriculaEscolar())
                .filter(outro -> !outro.getId().equals(id))
                .ifPresent(outro -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Já existe um estudante com esta matrícula nesta escola");
                });

        estudante.setNome(request.nome());
        estudante.setEmail(request.email());
        estudante.setMatriculaEscolar(request.matriculaEscolar());
        estudante.setAlergias(request.alergias());
        estudante.setEscola(escola);
        // Só troca a senha se uma nova foi enviada.
        if (request.senha() != null && !request.senha().isBlank()) {
            estudante.setSenha(senhaService.gerarHash(request.senha()));
        }
        return estudanteRepository.save(estudante);
    }

    @Transactional
    public void deletar(Long id) {
        Estudante estudante = buscarPorId(id);
        try {
            estudanteRepository.delete(estudante);
            estudanteRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "O estudante possui votos ou feedbacks vinculados e não pode ser excluído");
        }
    }

    private Escola buscarEscola(Long escolaId) {
        return escolaRepository.findById(escolaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Escola não encontrada com id: " + escolaId));
    }
}
