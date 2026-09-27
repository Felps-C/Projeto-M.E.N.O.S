package com.example.M.E.N.O.S.model;

import java.io.Serializable;

import jakarta.persistence.*;

        import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "Enquetes")

public class Enquete implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Boolean ativa;

    @OneToMany(mappedBy = "enquete", cascade = CascadeType.ALL)
    private List<OpcaoVoto> opcoes = new ArrayList<>();

    public Enquete() {
    }

    public Enquete(Long id, String titulo, String descricao, LocalDateTime dataInicio, LocalDateTime dataFim, Boolean ativa, List<OpcaoVoto> opcoes) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.ativa = ativa;
        this.opcoes = opcoes;
    }

    public Boolean AbrirEnquete(){
        this.ativa = true;
        return this.ativa;
    }

    public Boolean FecharEnquete(){
        this.ativa = false;
        return  this.ativa;
    }

    public void AdicionarOpcao(OpcaoVoto opcao){
        this.opcoes.add(opcao);
        opcao.setEnquete(this);
    }

    public Map CalcularResultado(){
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public List<OpcaoVoto> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<OpcaoVoto> opcoes) {
        this.opcoes = opcoes;
    }

    @Override
    public String toString() {
        return "Enquete{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", ativa=" + ativa +
                ", opcoes=" + opcoes +
                '}';
    }
}