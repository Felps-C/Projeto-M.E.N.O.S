package com.example.M.E.N.O.S.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Map;

@Entity
@Table(name = "OpcoesVoto")

public class OpcaoVoto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "enquete_id")
    private Enquete enquete;

    public OpcaoVoto() {

    }

    public OpcaoVoto(Long id, String descricao, Enquete enquete) {
        this.id = id;
        this.descricao = descricao;
        this.enquete = enquete;
    }

    public Boolean AbrirEnquete(){
        return enquete != null && enquete.AbrirEnquete();
    }

    public Boolean FecharEnquete(){
        return enquete != null && enquete.FecharEnquete();
    }

    public void AdicionarOpcao(OpcaoVoto opcao){
        if ( enquete != null){
            enquete.AdicionarOpcao(opcao);
        }
    }

    public Map CalcularResultado(){
        if ( enquete != null ){
            return enquete.CalcularResultado();
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Enquete getEnquete() {
        return enquete;
    }

    public void setEnquete(Enquete enquete) {
        this.enquete = enquete;
    }

    @Override
    public String toString() {
        return "OpcaoVoto{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", enquete=" + enquete +
                '}';
    }
}
