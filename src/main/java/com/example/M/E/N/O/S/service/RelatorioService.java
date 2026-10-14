package com.example.M.E.N.O.S.service;

import com.example.M.E.N.O.S.dto.RelatorioRequest;
import com.example.M.E.N.O.S.dto.RelatorioResponse;
import com.example.M.E.N.O.S.dto.ResultadoEnquete;
import com.example.M.E.N.O.S.model.Enquete;
import com.example.M.E.N.O.S.model.Escola;
import com.example.M.E.N.O.S.model.Feedback;
import com.example.M.E.N.O.S.model.Relatorio;
import com.example.M.E.N.O.S.repository.EnqueteRepository;
import com.example.M.E.N.O.S.repository.EscolaRepository;
import com.example.M.E.N.O.S.repository.FeedbackRepository;
import com.example.M.E.N.O.S.repository.RelatorioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioService {

    private static final int FEEDBACKS_RECENTES = 10;

    private final RelatorioRepository relatorioRepository;
    private final EscolaRepository escolaRepository;
    private final EnqueteRepository enqueteRepository;
    private final FeedbackRepository feedbackRepository;
    private final EnqueteService enqueteService;

    // Injeção de dependência via construtor
    public RelatorioService(RelatorioRepository relatorioRepository,
                            EscolaRepository escolaRepository,
                            EnqueteRepository enqueteRepository,
                            FeedbackRepository feedbackRepository,
                            EnqueteService enqueteService) {
        this.relatorioRepository = relatorioRepository;
        this.escolaRepository = escolaRepository;
        this.enqueteRepository = enqueteRepository;
        this.feedbackRepository = feedbackRepository;
        this.enqueteService = enqueteService;
    }

    // Gera e salva um relatório da escola, calculado a partir das enquetes e votos do banco.
    @Transactional
    public RelatorioResponse gerar(Long escolaId, RelatorioRequest request) {
        Escola escola = escolaRepository.findById(escolaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Escola não encontrada com id: " + escolaId));

        LocalDateTime inicio = request != null ? request.dataInicio() : null;
        LocalDateTime fim = request != null ? request.dataFim() : null;
        if (inicio != null && fim != null && !fim.isAfter(inicio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A data de fim deve ser posterior à data de início");
        }

        List<Enquete> enquetes = enqueteRepository.findByEscola_IdOrderByDataInicioDesc(escolaId).stream()
                .filter(e -> sobrepoe(e, inicio, fim))
                .toList();

        Relatorio relatorio = new Relatorio();
        relatorio.setEscola(escola);
        relatorio.setDataInicio(inicio);
        relatorio.setDataFim(fim);
        relatorio.compilarDados(enquetes);
        return montar(relatorioRepository.save(relatorio));
    }

    @Transactional(readOnly = true)
    public List<RelatorioResponse> listarPorEscola(Long escolaId) {
        if (!escolaRepository.existsById(escolaId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Escola não encontrada com id: " + escolaId);
        }
        return relatorioRepository.findByEscola_IdOrderByDataGeracaoDesc(escolaId).stream()
                .map(this::montar)
                .toList();
    }

    @Transactional(readOnly = true)
    public RelatorioResponse buscarPorId(Long id) {
        return montar(buscar(id));
    }

    @Transactional
    public void deletar(Long id) {
        relatorioRepository.delete(buscar(id));
    }

    // Resultado das enquetes do relatório com o status pedido ("ativa" ou "inativa").
    @Transactional(readOnly = true)
    public List<ResultadoEnquete> filtrarStatus(Long id, String status) {
        if (!"ativa".equalsIgnoreCase(status) && !"inativa".equalsIgnoreCase(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido. Use 'ativa' ou 'inativa'.");
        }
        return buscar(id).filtrarStatus(status).stream()
                .map(enqueteService::resultado)
                .toList();
    }

    // ---------- auxiliares ----------

    private Relatorio buscar(Long id) {
        return relatorioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Relatório não encontrado com id: " + id));
    }

    // A enquete entra se o período dela se sobrepõe ao intervalo do relatório
    // (início/fim nulos = sem limite daquele lado).
    private boolean sobrepoe(Enquete e, LocalDateTime inicio, LocalDateTime fim) {
        boolean comecaAntesDoFim = fim == null || e.getDataInicio() == null || !e.getDataInicio().isAfter(fim);
        boolean terminaDepoisDoInicio = inicio == null || e.getDataFim() == null || !e.getDataFim().isBefore(inicio);
        return comecaAntesDoFim && terminaDepoisDoInicio;
    }

    // Os resultados por enquete e os feedbacks são lidos do banco a cada consulta, então
    // sempre batem com os votos atuais. totalEnquete/totalVotos/mediaVotos são os valores
    // salvos na geração do relatório.
    private RelatorioResponse montar(Relatorio relatorio) {
        Long escolaId = relatorio.getEscola().getId();

        List<ResultadoEnquete> enquetes = relatorio.getEnquetes().stream()
                .map(enqueteService::resultado)
                .toList();

        LocalDate de = relatorio.getDataInicio() != null ? relatorio.getDataInicio().toLocalDate() : null;
        LocalDate ate = relatorio.getDataFim() != null ? relatorio.getDataFim().toLocalDate() : null;
        List<Feedback> feedbacks = feedbackRepository.findByMenu_Escola_IdOrderByDataDescIdDesc(escolaId).stream()
                .filter(f -> f.getData() == null
                        || ((de == null || !f.getData().isBefore(de)) && (ate == null || !f.getData().isAfter(ate))))
                .toList();

        Map<Long, Long> porCardapio = new LinkedHashMap<>();
        for (Feedback f : feedbacks) {
            porCardapio.merge(f.getMenuId(), 1L, Long::sum);
        }
        List<RelatorioResponse.ItemFeedback> recentes = feedbacks.stream()
                .limit(FEEDBACKS_RECENTES)
                .map(f -> new RelatorioResponse.ItemFeedback(f.getId(), f.getMenuId(), f.getData(), f.getTexto()))
                .toList();

        return new RelatorioResponse(
                relatorio.getId(),
                escolaId,
                relatorio.getDataGeracao(),
                relatorio.getDataInicio(),
                relatorio.getDataFim(),
                relatorio.getTotalEnquete(),
                relatorio.getTotalVotos(),
                relatorio.getMediaVotos(),
                enquetes,
                new RelatorioResponse.ResumoFeedbacks(feedbacks.size(), porCardapio, recentes));
    }
}
