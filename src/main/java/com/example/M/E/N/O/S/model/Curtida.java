package com.example.M.E.N.O.S.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

// Curtida de um estudante em um cardápio (RF12).
// A restrição única (estudante_id, menu_id) garante no banco que cada estudante
// curta um mesmo cardápio uma única vez.
@Entity
@Table(name = "Curtidas",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_curtida_estudante_menu",
                columnNames = {"estudante_id", "menu_id"}))
public class Curtida implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Estudante e cardápio não vão inteiros no JSON (o estudante tem e-mail e alergias);
    // saem só os ids, pelos getters abaixo.
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "estudante_id")
    private Estudante estudante;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    // Preenchida automaticamente pelo sistema na criação.
    private LocalDateTime data;

    public Curtida() {
    }

    public Curtida(Long id, Estudante estudante, Menu menu, LocalDateTime data) {
        this.id = id;
        this.estudante = estudante;
        this.menu = menu;
        this.data = data;
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

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @JsonProperty("estudanteId")
    public Long getEstudanteId() {
        return estudante != null ? estudante.getId() : null;
    }

    @JsonProperty("menuId")
    public Long getMenuId() {
        return menu != null ? menu.getId() : null;
    }

    // Só ids no toString: nada de estudante inteiro nem cardápio nos logs.
    @Override
    public String toString() {
        return "Curtida{" +
                "id=" + id +
                ", estudanteId=" + getEstudanteId() +
                ", menuId=" + getMenuId() +
                ", data=" + data +
                '}';
    }
}
