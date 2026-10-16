package com.example.M.E.N.O.S.service;

import com.example.M.E.N.O.S.dto.LoginRequest;
import com.example.M.E.N.O.S.dto.LoginResponse;
import com.example.M.E.N.O.S.model.Escola;
import com.example.M.E.N.O.S.model.Estudante;
import com.example.M.E.N.O.S.repository.EscolaRepository;
import com.example.M.E.N.O.S.repository.EstudanteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class AuthService {

    // Mesma mensagem para "não existe" e "senha errada", para não revelar quais contas existem.
    private static final String CREDENCIAIS_INVALIDAS = "Matrícula, CNPJ ou senha inválidos";

    private final EscolaRepository escolaRepository;
    private final EstudanteRepository estudanteRepository;
    private final SenhaService senhaService;

    public AuthService(EscolaRepository escolaRepository,
                       EstudanteRepository estudanteRepository,
                       SenhaService senhaService) {
        this.escolaRepository = escolaRepository;
        this.estudanteRepository = estudanteRepository;
        this.senhaService = senhaService;
    }

    @Transactional
    public LoginResponse entrar(LoginRequest request) {
        String identificador = request.identificador().trim();

        List<Escola> escolas = buscarEscolas(identificador);
        if (!escolas.isEmpty()) {
            return entrarComoEscola(escolas, request.senha());
        }
        Estudante estudante = buscarEstudante(identificador)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, CREDENCIAIS_INVALIDAS));
        return entrarComoEstudante(estudante, request.senha());
    }

    // ---------- escola ----------

    // Pode haver mais de uma escola com o mesmo identificador (dados antigos). Entra a que tiver a senha certa.
    private LoginResponse entrarComoEscola(List<Escola> candidatas, String senha) {
        List<Escola> certas = candidatas.stream()
                .filter(e -> senhaService.confere(senha, e.getSenha()))
                .toList();
        if (certas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, CREDENCIAIS_INVALIDAS);
        }
        if (certas.size() > 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Mais de uma escola tem este CNPJ e esta senha. Entre usando o e-mail da escola");
        }
        Escola escola = certas.get(0);
        if (senhaService.precisaAtualizar(escola.getSenha())) {
            escola.setSenha(senhaService.gerarHash(senha)); // troca texto puro por hash (salvo ao fim da transação)
        }
        return new LoginResponse("escola", escola.getId(), escola.getId(), escola.getNome());
    }

    private List<Escola> buscarEscolas(String identificador) {
        if (identificador.contains("@")) {
            return escolaRepository.findByEmail(identificador).map(List::of).orElse(List.of());
        }
        // Não exige 14 caracteres: escolas antigas podem ter um CNPJ fora do padrão e precisam entrar.
        // Primeiro tenta o texto exatamente como foi digitado; depois, sem pontos, barra e traço.
        List<Escola> exatas = escolaRepository.findAllByCnpj(identificador);
        if (!exatas.isEmpty()) {
            return exatas;
        }
        String cnpj = semPontuacao(identificador);
        if (cnpj.isEmpty()) {
            return List.of();
        }
        return escolaRepository.findAllByCnpjSemPontuacao(cnpj);
    }

    // ---------- aluno ----------

    private LoginResponse entrarComoEstudante(Estudante estudante, String senha) {
        if (!senhaService.confere(senha, estudante.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, CREDENCIAIS_INVALIDAS);
        }
        if (senhaService.precisaAtualizar(estudante.getSenha())) {
            estudante.setSenha(senhaService.gerarHash(senha));
        }
        return new LoginResponse("aluno", estudante.getId(), estudante.getEscola().getId(), estudante.getNome());
    }

    private Optional<Estudante> buscarEstudante(String identificador) {
        if (identificador.contains("@")) {
            return estudanteRepository.findByEmail(identificador);
        }
        List<Estudante> encontrados = estudanteRepository.findByMatriculaEscolar(identificador);
        if (encontrados.size() > 1) {
            // A matrícula só é única dentro de cada escola, então o e-mail resolve a dúvida.
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Esta matrícula existe em mais de uma escola. Entre usando o seu e-mail");
        }
        return encontrados.stream().findFirst();
    }

    private String semPontuacao(String texto) {
        return texto.replaceAll("[.\\-/\\s]", "").toUpperCase(Locale.ROOT);
    }
}
