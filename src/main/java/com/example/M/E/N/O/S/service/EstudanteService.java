package com.example.M.E.N.O.S.service;

import com.example.M.E.N.O.S.model.Estudante;
import com.example.M.E.N.O.S.repository.EstudanteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstudanteService {

    private final EstudanteRepository estudanteRepository;

    // Injeção de dependência via construtor (forma recomendada no Spring)
    public EstudanteService(EstudanteRepository estudanteRepository) {
        this.estudanteRepository = estudanteRepository;
    }

    public List<Estudante> listarTodos() {
        return estudanteRepository.findAll();
    }

    public Estudante buscarPorId(Long id) {
        return estudanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado com id: " + id));
    }

    public Estudante salvar(Estudante estudante) {
        // Exemplo de regra de negócio: não permitir dois estudantes com o mesmo email
        estudanteRepository.findByEmail(estudante.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Já existe um estudante com este email");
        });
        return estudanteRepository.save(estudante);
    }

    public Estudante atualizar(Long id, Estudante dadosAtualizados) {
        Estudante estudante = buscarPorId(id);
        estudante.setNome(dadosAtualizados.getNome());
        estudante.setEmail(dadosAtualizados.getEmail());
        return estudanteRepository.save(estudante);
    }

    public void deletar(Long id) {
        estudanteRepository.deleteById(id);
    }
}