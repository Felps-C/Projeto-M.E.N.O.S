package com.example.M.E.N.O.S.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

// A restrição única (estudante_id, enquete_id) garante no banco que
// cada estudante vote uma única vez em cada enquete.
@Entity
@Table(name = "Votos",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_voto_estudante_enquete",
                columnNames = {"estudante_id", "enquete_id"}))
public class Voto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Estudante, opção e enquete não vão inteiros no JSON (o estudante tem e-mail e alergias);
    // saem só os ids, pelos getters abaixo.
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "estudante_id")
    private Estudante estudante;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "opcao_id")
    private OpcaoVoto opcaoEscolhida;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "enquete_id")
    private Enquete enquete;

    private LocalDateTime dataVoto;

    public Voto() {
    }

    public Voto(Long id, Estudante estudante, OpcaoVoto opcaoEscolhida, LocalDateTime dataVoto) {
        this.id = id;
        this.estudante = estudante;
        this.opcaoEscolhida = opcaoEscolhida;
        this.dataVoto = dataVoto;
    }

    // Métodos abaixo mantidos do diagrama de classes (só repassam para a opção escolhida).
    public Boolean abrirEnquete(){
        if (opcaoEscolhida != null){
            return opcaoEscolhida.abrirEnquete();
        }
        return false;
    }

    public Boolean fecharEnquete(){
        if (opcaoEscolhida != null){
            return opcaoEscolhida.fecharEnquete();
        }
        return false;
    }

    public void adicionarOpcao(OpcaoVoto opcao){
        if (opcaoEscolhida != null) {
            opcaoEscolhida.adicionarOpcao(opcao);
        }
    }

    public Map<Long, Long> calcularResultado(){
        if (opcaoEscolhida != null){
            return opcaoEscolhida.calcularResultado();
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

    public Enquete getEnquete() {
        return enquete;
    }

    public void setEnquete(Enquete enquete) {
        this.enquete = enquete;
    }

    public LocalDateTime getDataVoto() {
        return dataVoto;
    }

    public void setDataVoto(LocalDateTime dataVoto) {
        this.dataVoto = dataVoto;
    }

    @JsonProperty("estudanteId")
    public Long getEstudanteId() {
        return estudante != null ? estudante.getId() : null;
    }

    @JsonProperty("opcaoId")
    public Long getOpcaoId() {
        return opcaoEscolhida != null ? opcaoEscolhida.getId() : null;
    }

    @JsonProperty("enqueteId")
    public Long getEnqueteId() {
        return enquete != null ? enquete.getId() : null;
    }

    // Só ids no toString: antes imprimia estudante e opção (e, por elas, a enquete inteira).
    @Override
    public String toString() {
        return "Voto{" +
                "id=" + id +
                ", estudanteId=" + getEstudanteId() +
                ", opcaoId=" + getOpcaoId() +
                ", enqueteId=" + getEnqueteId() +
                ", dataVoto=" + dataVoto +
                '}';
    }
}
