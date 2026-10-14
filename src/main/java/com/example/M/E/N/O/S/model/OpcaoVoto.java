package com.example.M.E.N.O.S.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "OpcoesVoto")
public class OpcaoVoto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    // @JsonIgnore evita loop infinito no JSON (Enquete -> OpcaoVoto -> Enquete -> ...)
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "enquete_id")
    private Enquete enquete;

    // Votos recebidos por esta opção (usado para contar o resultado).
    @JsonIgnore
    @OneToMany(mappedBy = "opcaoEscolhida")
    private List<Voto> votos = new ArrayList<>();

    public OpcaoVoto() {
    }

    public OpcaoVoto(String descricao) {
        this.descricao = descricao;
    }

    public OpcaoVoto(Long id, String descricao, Enquete enquete) {
        this.id = id;
        this.descricao = descricao;
        this.enquete = enquete;
    }

    // Métodos abaixo mantidos do diagrama de classes (só repassam para a Enquete).
    public Boolean abrirEnquete(){
        return enquete != null && enquete.abrirEnquete();
    }

    public Boolean fecharEnquete(){
        return enquete != null && enquete.fecharEnquete();
    }

    public void adicionarOpcao(OpcaoVoto opcao){
        if (enquete != null){
            enquete.adicionarOpcao(opcao);
        }
    }

    public Map<Long, Long> calcularResultado(){
        if (enquete != null){
            return enquete.calcularResultado();
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

    public List<Voto> getVotos() {
        return votos;
    }

    public void setVotos(List<Voto> votos) {
        this.votos = votos;
    }

    // Sem "enquete" e sem "votos" no toString: antes gerava StackOverflowError
    // (Enquete imprime as opções, e cada opção imprimia a enquete de volta).
    @Override
    public String toString() {
        return "OpcaoVoto{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
