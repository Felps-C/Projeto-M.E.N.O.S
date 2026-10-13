package com.example.M.E.N.O.S.model;

import jakarta.persistence.*;

import java.io.Serializable;

// Uma linha da lista de compras: qual produto e quanto precisa comprar.
// Antes a lista repetia o mesmo produto várias vezes; agora guarda a quantidade.
@Entity
@Table(name = "ItensListaCompra")
public class ItemListaCompra implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private int quantidadeNecessaria;

    public ItemListaCompra() {
    }

    public ItemListaCompra(Produto produto, int quantidadeNecessaria) {
        this.produto = produto;
        this.quantidadeNecessaria = quantidadeNecessaria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidadeNecessaria() {
        return quantidadeNecessaria;
    }

    public void setQuantidadeNecessaria(int quantidadeNecessaria) {
        this.quantidadeNecessaria = quantidadeNecessaria;
    }

    @Override
    public String toString() {
        return "ItemListaCompra{" +
                "id=" + id +
                ", produto=" + produto +
                ", quantidadeNecessaria=" + quantidadeNecessaria +
                '}';
    }
}
