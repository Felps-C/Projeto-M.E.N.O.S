package com.example.M.E.N.O.S.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "Produtos")
public class Produto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório")
    private String nome;

    private LocalDate validade;

    @PositiveOrZero(message = "A quantidade não pode ser negativa")
    private int quantidade;

    // Quantidade mínima desejada em estoque (RF08). 0 = alerta desativado.
    @PositiveOrZero(message = "O estoque mínimo não pode ser negativo")
    private int estoqueMinimo;

    // Categoria (ex.: Grãos, Proteína) e unidade de medida (ex.: kg, L). Opcionais.
    @Size(max = 60, message = "A categoria deve ter no máximo 60 caracteres")
    private String categoria;

    @Size(max = 20, message = "A unidade deve ter no máximo 20 caracteres")
    private String unidade;

    public Produto(){
    }

    public Produto(Long id, String nome, LocalDate validade, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.validade = validade;
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", validade=" + validade +
                ", quantidade=" + quantidade +
                ", estoqueMinimo=" + estoqueMinimo +
                ", categoria='" + categoria + '\'' +
                ", unidade='" + unidade + '\'' +
                '}';
    }
}
