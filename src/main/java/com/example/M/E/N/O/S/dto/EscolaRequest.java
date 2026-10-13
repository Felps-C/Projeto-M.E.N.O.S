package com.example.M.E.N.O.S.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Corpo para criar/atualizar uma escola:
// { "nome": "...", "email": "...", "senha": "...", "cnpj": "..." }
// Na criação a senha é obrigatória. Na atualização, se "senha" não for enviada,
// a senha atual é mantida.
public record EscolaRequest(
        @NotBlank(message = "O nome da escola é obrigatório") String nome,
        @NotBlank(message = "O e-mail é obrigatório") @Email(message = "E-mail inválido") String email,
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres") String senha,
        @NotBlank(message = "O CNPJ da escola é obrigatório") String cnpj
) {
}
