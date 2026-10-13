package com.example.M.E.N.O.S.dto;

import jakarta.validation.constraints.Positive;

// Corpo das requisições de entrada e saída: { "quantidade": 10 }
public record MovimentacaoRequest(
        @Positive(message = "A quantidade deve ser maior que zero") int quantidade
) {
}
