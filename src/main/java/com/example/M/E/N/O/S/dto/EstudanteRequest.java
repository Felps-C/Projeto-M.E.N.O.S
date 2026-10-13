package com.example.M.E.N.O.S.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Corpo para criar/atualizar um estudante:
// { "nome": "...", "email": "...", "senha": "...", "matriculaEscolar": "...",
//   "alergias": "...", "escolaId": 1 }
// Na criação a senha é obrigatória. Na atualização, se "senha" não for enviada,
// a senha atual é mantida. "alergias" é opcional.
public record EstudanteRequest(
        @NotBlank(message = "O nome é obrigatório") String nome,
        @NotBlank(message = "O e-mail é obrigatório") @Email(message = "E-mail inválido") String email,
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres") String senha,
        @NotBlank(message = "A matrícula escolar é obrigatória") String matriculaEscolar,
        @Size(max = 500, message = "As alergias devem ter no máximo 500 caracteres") String alergias,
        @NotNull(message = "A escola do estudante é obrigatória") Long escolaId
) {
}
