package com.example.M.E.N.O.S.dto;

import jakarta.validation.constraints.NotBlank;

// Corpo para adicionar uma opção a uma enquete: { "descricao": "Suco de caju" }
public record OpcaoRequest(
        @NotBlank(message = "A descrição da opção é obrigatória") String descricao
) {
}
