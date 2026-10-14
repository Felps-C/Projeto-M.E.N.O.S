package com.example.M.E.N.O.S.dto;

// Curtidas de um cardápio: total e, se o estudante foi informado (?estudanteId=),
// se ele já curtiu (curtiu = null quando o estudante não é informado).
public record CurtidasMenu(Long menuId, long total, Boolean curtiu) {
}
