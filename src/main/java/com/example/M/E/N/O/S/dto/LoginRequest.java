package com.example.M.E.N.O.S.dto;

import jakarta.validation.constraints.NotBlank;

// Corpo do login: { "identificador": "2024001", "senha": "minhaSenha" }
// "identificador" pode ser a matrícula (aluno), o CNPJ (escola) ou o e-mail (aluno ou escola).
public record LoginRequest(
        @NotBlank(message = "Informe a matrícula, o CNPJ ou o e-mail") String identificador,
        @NotBlank(message = "Informe a senha") String senha
) {
}
