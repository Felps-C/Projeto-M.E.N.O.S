package com.example.M.E.N.O.S.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table
public class Merenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Lob
    private byte[] imagem;
    private int quantidade;

    @ManyToMany(mappedBy = "merendas")
    private List<Menu> menus = new ArrayList<>();

    public Merenda(){
    }

    public Merenda(Long id, String nome, byte[] imagem, int quantidade, List<Menu> menus) {
        this.id = id;
        this.nome = nome;
        this.imagem = imagem;
        this.quantidade = quantidade;
        this.menus = menus;
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

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    @Override
    public String toString() {
        return "Merenda{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", imagem=" + Arrays.toString(imagem) +
                ", quantidade=" + quantidade +
                ", menus=" + menus +
                '}';
    }
}
