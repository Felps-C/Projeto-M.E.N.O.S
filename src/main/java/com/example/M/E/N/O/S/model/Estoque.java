package com.example.M.E.N.O.S.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Estoques")
public class Estoque implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cada escola tem um único estoque. A escola não vai inteira no JSON
    // (para não expor a senha); só o "escolaId", pelo getter abaixo.
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "escola_id", unique = true, nullable = false)
    private Escola escola;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "estoque_id")
    private List<Produto> produtos = new ArrayList<>();

    public Estoque(){
    }

    public Estoque(Escola escola) {
        this.escola = escola;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }

    @JsonProperty("escolaId")
    public Long getEscolaId() {
        return escola != null ? escola.getId() : null;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    @Override
    public String toString() {
        return "Estoque{" +
                "id=" + id +
                ", produtos=" + produtos +
                '}';
    }
}
