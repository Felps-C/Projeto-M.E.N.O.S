package com.example.M.E.N.O.S.dto;

import jakarta.validation.constraints.NotNull;

// Corpo para votar: { "estudanteId": 1, "opcaoId": 3 }
public record VotoRequest(
        @NotNull(message = "O estudante é obrigatório") Long estudanteId,
        @NotNull(message = "A opção escolhida é obrigatória") Long opcaoId
) {
}
