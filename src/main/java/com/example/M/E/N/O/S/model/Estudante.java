package com.example.M.E.N.O.S.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.io.Serializable;

@Entity
@Table(name = "Estudantes")
public class Estudante implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String matriculaEscolar;
    private String nome;
    private String senha;
    @Email
    private String email;

    public Estudante(){
    }

    public Estudante(Long id, String matriculaEscolar, String nome, String senha, String email) {
        this.id = id;
        this.matriculaEscolar = matriculaEscolar;
        this.nome = nome;
        this.senha = senha;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatriculaEscolar() {
        return matriculaEscolar;
    }

    public void setMatriculaEscolar(String matriculaEscolar) {
        this.matriculaEscolar = matriculaEscolar;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Estudante{" +
                "id=" + id +
                ", matriculaEscolar='" + matriculaEscolar + '\'' +
                ", nome='" + nome + '\'' +
                ", senha='" + senha + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}