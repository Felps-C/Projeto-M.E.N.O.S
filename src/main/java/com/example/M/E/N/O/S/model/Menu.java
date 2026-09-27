package com.example.M.E.N.O.S.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dataInicio;
    private Boolean ativa;

    @ManyToMany
    private List<Merenda> merendas = new ArrayList<>();

    public Menu(){
    }

    public Menu(Long id, LocalDate dataInicio, Boolean ativa, List<Merenda> merendas) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.ativa = ativa;
        this.merendas = merendas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public List<Merenda> getMerendas() {
        return merendas;
    }

    public void setMerendas(List<Merenda> merendas) {
        this.merendas = merendas;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", dataInicio=" + dataInicio +
                ", ativa=" + ativa +
                ", merendas=" + merendas +
                '}';
    }
}
