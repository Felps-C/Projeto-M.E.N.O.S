package com.example.M.E.N.O.S.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "Votos")

public class Voto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "estudante_id")
    private  Estudante estudante;

    @ManyToOne
    @JoinColumn(name = "opcao_id")
    private  OpcaoVoto opcaoEscolhida;
    private LocalDateTime dataVoto;

    public Voto() {

    }

    public Voto(Long id, Estudante estudante, OpcaoVoto opcaoEscolhida, LocalDateTime dataVoto) {
        this.id = id;
        this.estudante = estudante;
        this.opcaoEscolhida = opcaoEscolhida;
        this.dataVoto = dataVoto;
    }

    public Boolean AbrirEnquete(){
        if(opcaoEscolhida != null){
            return  opcaoEscolhida.AbrirEnquete();
        }
        return false;
    }

    public Boolean FecharEnquete(){
        if(opcaoEscolhida != null){
            return opcaoEscolhida.FecharEnquete();
        }
        return false;
    }

    public void AdicionarOpcao(OpcaoVoto opcao){
        if( opcaoEscolhida != null) {
            opcaoEscolhida.AdicionarOpcao(opcao);
        }
    }

    public Map CalcularResultado(){
        if ( opcaoEscolhida != null){
            return opcaoEscolhida.CalcularResultado();
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }

    public OpcaoVoto getOpcaoEscolhida() {
        return opcaoEscolhida;
    }

    public void setOpcaoEscolhida(OpcaoVoto opcaoEscolhida) {
        this.opcaoEscolhida = opcaoEscolhida;
    }

    public LocalDateTime getDataVoto() {
        return dataVoto;
    }

    public void setDataVoto(LocalDateTime dataVoto) {
        this.dataVoto = dataVoto;
    }

    @Override
    public String toString() {
        return "Voto{" +
                "id=" + id +
                ", estudante=" + estudante +
                ", opcaoEscolhida=" + opcaoEscolhida +
                ", dataVoto=" + dataVoto +
                '}';
    }
}
