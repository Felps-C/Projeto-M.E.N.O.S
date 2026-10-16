package com.example.M.E.N.O.S.dto;

// Números do card de um cardápio na tela do aluno.
// curtiu = null quando o estudante não é informado (?estudanteId=).
public record ResumoCardapio(Long menuId, long curtidas, Boolean curtiu, long comentarios) {
}
