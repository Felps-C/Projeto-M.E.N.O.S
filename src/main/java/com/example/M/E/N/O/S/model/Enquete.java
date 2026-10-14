package com.example.M.E.N.O.S.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "Enquetes")
public class Enquete implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String descricao;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    private Boolean ativa;

    // Escola que criou a enquete. Não vai inteira no JSON; só o "escolaId", pelo getter abaixo.
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "escola_id")
    private Escola escola;

    @OneToMany(mappedBy = "enquete", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<OpcaoVoto> opcoes = new ArrayList<>();

    public Enquete() {
    }

    public Enquete(Long id, String titulo, String descricao, LocalDateTime dataInicio, LocalDateTime dataFim, Boolean ativa, List<OpcaoVoto> opcoes) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.ativa = ativa;
        this.opcoes = opcoes;
    }

    public Boolean abrirEnquete(){
        this.ativa = true;
        return this.ativa;
    }

    public Boolean fecharEnquete(){
        this.ativa = false;
        return this.ativa;
    }

    public void adicionarOpcao(OpcaoVoto opcao){
        this.opcoes.add(opcao);
        opcao.setEnquete(this);
    }

    // Quantidade de votos de cada opção: { idDaOpcao -> votos }
    public Map<Long, Long> calcularResultado(){
        Map<Long, Long> resultado = new LinkedHashMap<>();
        for (OpcaoVoto opcao : opcoes) {
            resultado.put(opcao.getId(), (long) opcao.getVotos().size());
        }
        return resultado;
    }

    // Enquete "aberta" = marcada como ativa E dentro do período (início/fim).
    // Aparece no JSON como "aberta".
    @JsonProperty("aberta")
    public boolean isAberta() {
        LocalDateTime agora = LocalDateTime.now();
        return Boolean.TRUE.equals(ativa)
                && (dataInicio == null || !agora.isBefore(dataInicio))
                && (dataFim == null || !agora.isAfter(dataFim));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
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

    public List<OpcaoVoto> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<OpcaoVoto> opcoes) {
        this.opcoes = opcoes;
    }

    @Override
    public String toString() {
        return "Enquete{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", ativa=" + ativa +
                ", opcoes=" + opcoes +
                '}';
    }
}
