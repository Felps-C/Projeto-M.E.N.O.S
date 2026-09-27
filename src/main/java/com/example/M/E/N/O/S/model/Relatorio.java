package com.example.M.E.N.O.S.model;

import jakarta.persistence.*;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table( name = "Relatorios")

public class Relatorio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private Date dataGeracao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private int totalEnquete;
    private double mediaVotos;

    @OneToMany
    private List<Enquete> enquetes = new ArrayList<>();

    public Relatorio() {
    }

    public Relatorio(Long id, Date dataGeracao, LocalDateTime dataInicio, LocalDateTime dataFim, int totalEnquete, double mediaVotos, List<Enquete> enquetes) {
        this.id = id;
        this.dataGeracao = dataGeracao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.totalEnquete = totalEnquete;
        this.mediaVotos = mediaVotos;
        this.enquetes = enquetes;
    }

    public void CompilarDados(List<Enquete> enquetes){
        this.enquetes = enquetes;
        this.totalEnquete = enquetes.size();
        this.dataGeracao = new Date();
    }

    public List<Enquete> FiltrarStatus(String status) {
        List<Enquete> resultado = new ArrayList<>();
        for (Enquete enquete : enquetes) {
            if (status.equalsIgnoreCase("ativa") && Boolean.TRUE.equals(enquete.getAtiva())) {
                resultado.add(enquete);
            } else if (status.equalsIgnoreCase("inativa") && Boolean.FALSE.equals(enquete.getAtiva())) {
                resultado.add(enquete);
            }
        }
        return resultado;
    }

    public File Exportar () {
        return null;
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id){
        this.id = id;
    }

    public Date getDataGeracao () {
        return dataGeracao;
    }

    public void setDataGeracao (Date dataGeracao){
        this.dataGeracao = dataGeracao;
    }

    public LocalDateTime getDataInicio () {
        return dataInicio;
    }

    public void setDataInicio (LocalDateTime dataInicio){
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim () {
        return dataFim;
    }

    public void setDataFim (LocalDateTime dataFim){
        this.dataFim = dataFim;
    }

    public int getTotalEnquete () {
        return totalEnquete;
    }

    public void setTotalEnquete ( int totalEnquete){
        this.totalEnquete = totalEnquete;
    }

    public double getMediaVotos () {
        return mediaVotos;
    }

    public void setMediaVotos ( double mediaVotos){
        this.mediaVotos = mediaVotos;
    }

    public List<Enquete> getEnquetes () {
        return enquetes;
    }

    public void setEnquetes (List < Enquete > enquetes) {
        this.enquetes = enquetes;
    }

    @Override
    public String toString() {
        return "Relatorio{" +
                "id=" + id +
                ", dataGeracao=" + dataGeracao +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", totalEnquete=" + totalEnquete +
                ", mediaVotos=" + mediaVotos +
                ", enquetes=" + enquetes +
                '}';
    }
}