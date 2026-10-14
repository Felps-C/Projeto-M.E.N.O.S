package com.example.M.E.N.O.S.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Corpo do feedback: { "texto": "Gostei do cuscuz!", "estudanteId": 1, "menuId": 2 }
// Na criação, estudanteId e menuId são obrigatórios.
// Na edição (PUT), só o "texto" é usado; o autor e o cardápio não mudam.
public record FeedbackRequest(
        @NotBlank(message = "O texto do feedback é obrigatório")
        @Size(max = 255, message = "O texto deve ter no máximo 255 caracteres")
        String texto,
        Long estudanteId,
        Long menuId
) {
}
