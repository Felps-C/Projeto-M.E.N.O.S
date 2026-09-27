package com.example.M.E.N.O.S.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "Feedbacks")

public class Feedback implements Serializable {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String texto;
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "estudante_id")
    private Estudante estudante;

    @ManyToOne
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

    public void Cadastrar(){

    }
    public void Editar(){

    }
    public void Excluir(){

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

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", texto='" + texto + '\'' +
                ", data=" + data +
                ", estudante=" + estudante +
                ", menu=" + menu +
                '}';
    }
}
