package com.example.M.E.N.O.S.dto;

import jakarta.validation.constraints.NotNull;

// Corpo para curtir um cardápio: { "estudanteId": 1, "menuId": 2 }
public record CurtidaRequest(
        @NotNull(message = "O estudante é obrigatório") Long estudanteId,
        @NotNull(message = "O cardápio é obrigatório") Long menuId
) {
}
