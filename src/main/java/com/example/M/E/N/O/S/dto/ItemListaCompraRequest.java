package com.example.M.E.N.O.S.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// Corpo para adicionar um item na lista: { "produtoId": 1, "quantidade": 5 }
public record ItemListaCompraRequest(
        @NotNull(message = "O produto é obrigatório") Long produtoId,
        @Positive(message = "A quantidade deve ser maior que zero") int quantidade
) {
}
