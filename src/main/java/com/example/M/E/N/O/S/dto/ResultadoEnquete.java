package com.example.M.E.N.O.S.dto;

import java.util.List;

// Resultado da enquete: total de votos e, por opção, votos e percentual.
public record ResultadoEnquete(
        Long enqueteId,
        String titulo,
        boolean aberta,
        long totalVotos,
        List<ResultadoOpcao> opcoes
) {
    public record ResultadoOpcao(Long opcaoId, String descricao, long votos, double percentual) {
    }
}
