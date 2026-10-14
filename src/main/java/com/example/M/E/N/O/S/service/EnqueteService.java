package com.example.M.E.N.O.S.service;

import com.example.M.E.N.O.S.dto.EnqueteRequest;
import com.example.M.E.N.O.S.dto.EnqueteUpdateRequest;
import com.example.M.E.N.O.S.dto.OpcaoRequest;
import com.example.M.E.N.O.S.dto.ResultadoEnquete;
import com.example.M.E.N.O.S.model.Enquete;
import com.example.M.E.N.O.S.model.Escola;
import com.example.M.E.N.O.S.model.OpcaoVoto;
import com.example.M.E.N.O.S.repository.EnqueteRepository;
import com.example.M.E.N.O.S.repository.EscolaRepository;
import com.example.M.E.N.O.S.repository.VotoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class EnqueteService {

    private final EnqueteRepository enqueteRepository;
    private final EscolaRepository escolaRepository;
    private final VotoRepository votoRepository;

    // Injeção de dependência via construtor
    public EnqueteService(EnqueteRepository enqueteRepository,
                          EscolaRepository escolaRepository,
                          VotoRepository votoRepository) {
        this.enqueteRepository = enqueteRepository;
        this.escolaRepository = escolaRepository;
        this.votoRepository = votoRepository;
    }

    // ---------- Enquete ----------

    @Transactional
    public Enquete criar(Long escolaId, EnqueteRequest request) {
        Escola escola = escolaRepository.findById(escolaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Escola não encontrada com id: " + escolaId));

        LocalDateTime inicio = request.dataInicio() != null ? request.dataInicio() : LocalDateTime.now();
        validarPeriodo(inicio, request.dataFim());

        List<String> descricoes = request.opcoes().stream().map(String::trim).toList();
        long distintas = descricoes.stream().map(String::toLowerCase).distinct().count();
        if (distintas != descricoes.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "As opções da enquete não podem se repetir");
        }

        Enquete enquete = new Enquete();
        enquete.setEscola(escola);
        enquete.setTitulo(request.titulo());
        enquete.setDescricao(request.descricao());
        enquete.setDataInicio(inicio);
        enquete.setDataFim(request.dataFim());
        enquete.setAtiva(request.ativa() == null || request.ativa());
        descricoes.forEach(d -> enquete.adicionarOpcao(new OpcaoVoto(d)));
        return enqueteRepository.save(enquete);
    }

    // Com ?ativa=true devolve só as marcadas como ativas (é o que o aluno enxerga).
    @Transactional(readOnly = true)
    public List<Enquete> listarPorEscola(Long escolaId, Boolean ativa) {
        if (!escolaRepository.existsById(escolaId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Escola não encontrada com id: " + escolaId);
        }
        if (ativa == null) {
            return enqueteRepository.findByEscola_IdOrderByDataInicioDesc(escolaId);
        }
        return enqueteRepository.findByEscola_IdAndAtivaOrderByDataInicioDesc(escolaId, ativa);
    }

    @Transactional(readOnly = true)
    public Enquete buscarPorId(Long id) {
        return enqueteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Enquete não encontrada com id: " + id));
    }

    @Transactional
    public Enquete atualizar(Long id, EnqueteUpdateRequest request) {
        Enquete enquete = buscarPorId(id);
        LocalDateTime inicio = request.dataInicio() != null ? request.dataInicio() : enquete.getDataInicio();
        validarPeriodo(inicio, request.dataFim());

        enquete.setTitulo(request.titulo());
        enquete.setDescricao(request.descricao());
        enquete.setDataInicio(inicio);
        enquete.setDataFim(request.dataFim());
        return enqueteRepository.save(enquete);
    }

    // Enquete que já recebeu votos não pode ser apagada (os votos alimentam os relatórios).
    // Nesse caso, use "fechar".
    @Transactional
    public void deletar(Long id) {
        Enquete enquete = buscarPorId(id);
        if (votoRepository.existsByEnquete_Id(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A enquete já recebeu votos e não pode ser excluída. Feche a enquete em vez disso");
        }
        enqueteRepository.delete(enquete);
    }

    // ---------- Opções ----------

    @Transactional
    public Enquete adicionarOpcao(Long enqueteId, OpcaoRequest request) {
        Enquete enquete = buscarPorId(enqueteId);
        String descricao = request.descricao().trim();
        boolean repetida = enquete.getOpcoes().stream()
                .anyMatch(o -> o.getDescricao().equalsIgnoreCase(descricao));
        if (repetida) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma opção com esta descrição");
        }
        enquete.adicionarOpcao(new OpcaoVoto(descricao));
        return enqueteRepository.saveAndFlush(enquete);
    }

    @Transactional
    public Enquete removerOpcao(Long enqueteId, Long opcaoId) {
        Enquete enquete = buscarPorId(enqueteId);
        OpcaoVoto opcao = enquete.getOpcoes().stream()
                .filter(o -> o.getId().equals(opcaoId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "A opção " + opcaoId + " não pertence à enquete " + enqueteId));
        if (votoRepository.existsByOpcaoEscolhida_Id(opcaoId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A opção já recebeu votos e não pode ser removida");
        }
        if (enquete.getOpcoes().size() <= 2) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A enquete precisa ter pelo menos 2 opções");
        }
        enquete.getOpcoes().remove(opcao);
        return enqueteRepository.saveAndFlush(enquete);
    }

    // ---------- Abrir / fechar ----------

    @Transactional
    public Enquete abrir(Long id) {
        Enquete enquete = buscarPorId(id);
        if (enquete.getDataFim() != null && enquete.getDataFim().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A data de término já passou. Atualize a data de fim antes de abrir a enquete");
        }
        enquete.abrirEnquete();
        return enqueteRepository.save(enquete);
    }

    @Transactional
    public Enquete fechar(Long id) {
        Enquete enquete = buscarPorId(id);
        enquete.fecharEnquete();
        return enqueteRepository.save(enquete);
    }

    // ---------- Resultado ----------

    @Transactional(readOnly = true)
    public ResultadoEnquete resultado(Long id) {
        return resultado(buscarPorId(id));
    }

    // Também usado pelo relatório (RelatorioService).
    @Transactional(readOnly = true)
    public ResultadoEnquete resultado(Enquete enquete) {
        Map<Long, Long> contagem = enquete.calcularResultado();
        long total = contagem.values().stream().mapToLong(Long::longValue).sum();

        List<ResultadoEnquete.ResultadoOpcao> opcoes = enquete.getOpcoes().stream()
                .map(o -> {
                    long votos = contagem.getOrDefault(o.getId(), 0L);
                    double percentual = total == 0 ? 0.0 : Math.round(votos * 1000.0 / total) / 10.0;
                    return new ResultadoEnquete.ResultadoOpcao(o.getId(), o.getDescricao(), votos, percentual);
                })
                .toList();

        return new ResultadoEnquete(enquete.getId(), enquete.getTitulo(), enquete.isAberta(), total, opcoes);
    }

    // ---------- auxiliar ----------

    private void validarPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        if (fim != null && inicio != null && !fim.isAfter(inicio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A data de fim deve ser posterior à data de início");
        }
    }
}
