package com.example.M.E.N.O.S.dto;

// Resposta do login. É exatamente o que o front-end guarda na sessão.
// tipo = "escola" ou "aluno". Para a escola, id e escolaId são iguais.
public record LoginResponse(String tipo, Long id, Long escolaId, String nome) {
}
