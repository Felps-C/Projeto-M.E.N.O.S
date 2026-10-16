package com.example.M.E.N.O.S.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

// Corpo para atualizar os dados da enquete (as opções têm endpoints próprios,
// para não bagunçar votos já registrados).
public record EnqueteUpdateRequest(
        @NotBlank(message = "O título da enquete é obrigatório") String titulo,
        String descricao,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        Long merendaId // opcional: se vier, troca o prato vinculado; se não vier, mantém o atual
) {
}
