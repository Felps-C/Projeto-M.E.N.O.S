package com.example.M.E.N.O.S.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

// Corpo para criar/atualizar um cardápio:
// { "dataInicio": "2026-10-19", "ativa": true, "merendaIds": [1, 2, 3] }
// "ativa" é opcional (se não vier, o cardápio é criado como ativo).
public record MenuRequest(
        @NotNull(message = "A data de início é obrigatória") LocalDate dataInicio,
        Boolean ativa,
        List<Long> merendaIds
) {
}
