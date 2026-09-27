package com.example.M.E.N.O.S.model;

import jakarta.persistence.*;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ListasCompras")

public class ListaCompra implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private LocalDateTime dataCriacao;

    @ManyToMany
    private List<Produto> itens = new ArrayList<>();

    @ManyToOne
    @JoinColumn( name = "estoque_id")
    private Estoque estoque;

    public ListaCompra() {

    }

    public ListaCompra(Long id, String status, LocalDateTime dataCriacao, List<Produto> itens) {
        this.id = id;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.itens = itens;
    }

    public  ListaCompra gerarLista(List<Produto> produtos) {
        this.itens = produtos;
        this.dataCriacao = LocalDateTime.now();
        return  this;
    }

    public void adicionarItem(Produto produto, int quantidadeNecessaria){
        if ( produto != null && quantidadeNecessaria > 0 ) {
            for (int i = 0; i < quantidadeNecessaria; i++){
                this.itens.add(produto);
            }
        }
    }

    public void removerItem(Long produtoId){
        this.itens.removeIf(produto -> produto.getId().equals(produtoId));
    }

    public File exportar(String formato){
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }

    public List<Produto> getItens() {
        return itens;
    }

    public void setItens(List<Produto> itens) {
        this.itens = itens;
    }

    @Override
    public String toString() {
        return "ListaCompra{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", itens=" + itens +
                ", estoque=" + estoque +
                '}';
    }
}
