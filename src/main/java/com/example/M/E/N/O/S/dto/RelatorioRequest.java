package com.example.M.E.N.O.S.dto;

import java.time.LocalDateTime;

// Corpo para gerar um relatório da escola:
// { "dataInicio": "2026-10-01T00:00:00", "dataFim": "2026-10-31T23:59:59" }
// Os dois campos são opcionais (sem eles, entram todas as enquetes da escola).
// Entram as enquetes cujo período se sobrepõe a este intervalo.
// Totais e médias NÃO vêm do cliente: o servidor calcula.
public record RelatorioRequest(
        LocalDateTime dataInicio,
        LocalDateTime dataFim
) {
}
