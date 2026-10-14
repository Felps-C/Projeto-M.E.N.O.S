package com.example.M.E.N.O.S.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

// Relatório pronto para exibir: totais, resultado de cada enquete e resumo dos feedbacks.
public record RelatorioResponse(
        Long id,
        Long escolaId,
        Date dataGeracao,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        int totalEnquete,
        long totalVotos,
        double mediaVotos,
        List<ResultadoEnquete> enquetes,
        ResumoFeedbacks feedbacks
) {
    // total no período, quantidade por cardápio ({ menuId -> quantidade }) e os mais recentes.
    public record ResumoFeedbacks(long total, Map<Long, Long> porCardapio, List<ItemFeedback> recentes) {
    }

    // Sem o estudante: o relatório é da gestão, não precisa identificar quem comentou.
    public record ItemFeedback(Long id, Long menuId, LocalDate data, String texto) {
    }
}
