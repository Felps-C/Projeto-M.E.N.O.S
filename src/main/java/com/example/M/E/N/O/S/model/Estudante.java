package com.example.M.E.N.O.S.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

@Entity
@Table(name = "Estudantes")
public class Estudante implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A matrícula escolar é obrigatória")
    private String matriculaEscolar;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    // WRITE_ONLY: a senha pode ser recebida, mas nunca aparece nas respostas JSON.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Column(unique = true)
    private String email;

    // Alergias informadas na tela "Saúde e Alergias" (texto livre, opcional).
    @Column(length = 500)
    private String alergias;

    // Escola do estudante. Não vai inteira no JSON; só o "escolaId", pelo getter abaixo.
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "escola_id")
    private Escola escola;

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

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
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

    // Sem senha e sem alergias no toString, para não aparecerem em logs.
    @Override
    public String toString() {
        return "Estudante{" +
                "id=" + id +
                ", matriculaEscolar='" + matriculaEscolar + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}