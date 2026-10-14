package com.example.M.E.N.O.S.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

// Corpo para criar uma enquete:
// { "titulo": "Qual lanche vocês preferem?", "descricao": "...",
//   "dataInicio": "2026-10-20T08:00:00", "dataFim": "2026-10-25T18:00:00",
//   "ativa": true, "opcoes": ["Cuscuz com ovo", "Pão com carne moída"] }
// dataInicio (padrão: agora), dataFim e ativa (padrão: true) são opcionais.
public record EnqueteRequest(
        @NotBlank(message = "O título da enquete é obrigatório") String titulo,
        String descricao,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        Boolean ativa,
        @NotNull(message = "Informe as opções de voto")
        @Size(min = 2, message = "A enquete precisa ter pelo menos 2 opções")
        List<@NotBlank(message = "Nenhuma opção pode estar em branco") String> opcoes
) {
}
