package com.example.M.E.N.O.S.dto;

import com.example.M.E.N.O.S.model.Produto;

import java.util.List;

// Resultado dos alertas do estoque (RF08).
public record AlertasEstoque(
        int diasParaVencer,
        List<Produto> vencidos,
        List<Produto> aVencer,
        List<Produto> abaixoDoMinimo
) {
}
