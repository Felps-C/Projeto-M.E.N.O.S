package com.example.M.E.N.O.S.model;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "estoque_id")
    private List<Produto> produtos = new ArrayList<>();

    public Estoque(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
