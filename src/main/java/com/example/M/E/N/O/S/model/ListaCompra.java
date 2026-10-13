package com.example.M.E.N.O.S.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ListasCompras")
public class ListaCompra implements Serializable {

    public static final String ABERTA = "ABERTA";
    public static final String CONCLUIDA = "CONCLUIDA";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;

    private LocalDateTime dataCriacao;

    // Cada item tem produto + quantidade (antes era só uma lista de produtos).
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "lista_compra_id")
    private List<ItemListaCompra> itens = new ArrayList<>();

    // O estoque não vai inteiro no JSON; só o "estoqueId", pelo getter abaixo.
    @JsonIgnore
    @ManyToOne
    @JoinColumn( name = "estoque_id")
    private Estoque estoque;

    public ListaCompra() {
    }

    public ListaCompra(Estoque estoque) {
        this.estoque = estoque;
        this.status = ABERTA;
        this.dataCriacao = LocalDateTime.now();
    }

    // Gera a lista automática: pega os produtos que estão no estoque mínimo ou abaixo dele
    // e calcula quanto comprar para chegar em (estoqueMinimo x fator).
    // Ex.: mínimo 10, tem 4, fator 2 -> comprar 16 (fica com 20).
    public ListaCompra gerarLista(List<Produto> produtos, int fator) {
        this.itens.clear();
        this.status = ABERTA;
        this.dataCriacao = LocalDateTime.now();
        for (Produto produto : produtos) {
            if (produto.getEstoqueMinimo() > 0 && produto.getQuantidade() <= produto.getEstoqueMinimo()) {
                int necessaria = produto.getEstoqueMinimo() * fator - produto.getQuantidade();
                adicionarItem(produto, necessaria);
            }
        }
        return this;
    }

    // Se o produto já está na lista, soma a quantidade em vez de repetir a linha.
    public void adicionarItem(Produto produto, int quantidadeNecessaria){
        if (produto == null || quantidadeNecessaria <= 0) {
            return;
        }
        for (ItemListaCompra item : this.itens) {
            if (item.getProduto().getId().equals(produto.getId())) {
                item.setQuantidadeNecessaria(item.getQuantidadeNecessaria() + quantidadeNecessaria);
                return;
            }
        }
        this.itens.add(new ItemListaCompra(produto, quantidadeNecessaria));
    }

    public void removerItem(Long produtoId){
        this.itens.removeIf(item -> item.getProduto().getId().equals(produtoId));
    }

    // Exportação em PDF/Excel será feita no RF15.
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

    @JsonProperty("estoqueId")
    public Long getEstoqueId() {
        return estoque != null ? estoque.getId() : null;
    }

    public List<ItemListaCompra> getItens() {
        return itens;
    }

    public void setItens(List<ItemListaCompra> itens) {
        this.itens = itens;
    }

    @Override
    public String toString() {
        return "ListaCompra{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", itens=" + itens +
                '}';
    }
}
