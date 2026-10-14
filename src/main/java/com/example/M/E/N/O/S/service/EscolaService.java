package com.example.M.E.N.O.S.service;

import com.example.M.E.N.O.S.dto.EscolaRequest;
import com.example.M.E.N.O.S.model.Escola;
import com.example.M.E.N.O.S.repository.EscolaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EscolaService {

    private final EscolaRepository escolaRepository;

    // Injeção de dependência via construtor (forma recomendada no Spring)
    public EscolaService(EscolaRepository escolaRepository) {
        this.escolaRepository = escolaRepository;
    }

    public List<Escola> listarTodos() {
        return escolaRepository.findAll();
    }

    public Escola buscarPorId(Long id) {
        return escolaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Escola não encontrada com id: " + id));
    }

    @Transactional
    public Escola salvar(EscolaRequest request) {
        if (request.senha() == null || request.senha().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha é obrigatória");
        }
        escolaRepository.findByEmail(request.email()).ifPresent(outra -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma escola com este e-mail");
        });
        escolaRepository.findByCnpj(request.cnpj()).ifPresent(outra -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma escola com este CNPJ");
        });
        Escola escola = new Escola(request.nome(), request.email(), request.senha(), request.cnpj());
        return escolaRepository.save(escola);
    }

    @Transactional
    public Escola atualizar(Long id, EscolaRequest request) {
        Escola escola = buscarPorId(id);

        // O e-mail e o CNPJ não podem ser de OUTRA escola (a própria escola pode manter os seus).
        escolaRepository.findByEmail(request.email())
                .filter(outra -> !outra.getId().equals(id))
                .ifPresent(outra -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma escola com este e-mail");
                });
        escolaRepository.findByCnpj(request.cnpj())
                .filter(outra -> !outra.getId().equals(id))
                .ifPresent(outra -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma escola com este CNPJ");
                });

        escola.setNome(request.nome());
        escola.setEmail(request.email());
        escola.setCnpj(request.cnpj());
        // Só troca a senha se uma nova foi enviada.
        if (request.senha() != null && !request.senha().isBlank()) {
            escola.setSenha(request.senha());
        }
        return escolaRepository.save(escola);
    }

    @Transactional
    public void deletar(Long id) {
        Escola escola = buscarPorId(id);
        try {
            escolaRepository.delete(escola);
            escolaRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A escola possui estoque, cardápios ou estudantes vinculados e não pode ser excluída");
        }
    }
}
