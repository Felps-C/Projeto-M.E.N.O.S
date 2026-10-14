package com.example.M.E.N.O.S.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "Feedbacks")
public class Feedback implements Serializable {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O texto do feedback é obrigatório")
    private String texto;

    // Preenchida automaticamente pelo sistema na criação.
    private LocalDate data;

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

    public Feedback() {
    }

    public Feedback(Long id, String texto, LocalDate data, Estudante estudante, Menu menu) {
        this.id = id;
        this.texto = texto;
        this.data = data;
        this.estudante = estudante;
        this.menu = menu;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
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

    @JsonProperty("estudanteId")
    public Long getEstudanteId() {
        return estudante != null ? estudante.getId() : null;
    }

    @JsonProperty("menuId")
    public Long getMenuId() {
        return menu != null ? menu.getId() : null;
    }

    // Só ids no toString: antes imprimia o estudante inteiro e o cardápio.
    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", texto='" + texto + '\'' +
                ", data=" + data +
                ", estudanteId=" + getEstudanteId() +
                ", menuId=" + getMenuId() +
                '}';
    }
}
