package com.example.M.E.N.O.S.model;

import jakarta.persistence.*;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Relatório consolidado de uma escola. Quem calcula os números é o servidor
// (compilarDados), nunca o cliente: por isso não há setters para totalEnquete,
// totalVotos nem mediaVotos.
@Entity
@Table(name = "Relatorios")
public class Relatorio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dataGeracao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private int totalEnquete;
    private long totalVotos;
    private double mediaVotos;

    // Escola dona do relatório.
    @ManyToOne(optional = false)
    @JoinColumn(name = "escola_id")
    private Escola escola;

    // @ManyToMany: a mesma enquete pode aparecer em vários relatórios.
    // Com @OneToMany o Hibernate cria uma restrição UNIQUE em enquetes_id, e o segundo
    // relatório com a mesma enquete falhava. O nome da tabela é explícito de propósito:
    // o nome padrão antigo ("Relatorios_Enquetes") já tem essa restrição em bancos existentes.
    @ManyToMany
    @JoinTable(name = "relatorio_enquete",
            joinColumns = @JoinColumn(name = "relatorio_id"),
            inverseJoinColumns = @JoinColumn(name = "enquete_id"))
    @OrderBy("dataInicio DESC")
    private List<Enquete> enquetes = new ArrayList<>();

    public Relatorio() {
    }

    // Recebe as enquetes (já lidas do banco) e calcula os totais.
    // mediaVotos = votos por enquete (totalVotos / totalEnquete), com 1 casa decimal.
    public void compilarDados(List<Enquete> enquetes) {
        this.enquetes = new ArrayList<>(enquetes);
        this.totalEnquete = enquetes.size();

        long votos = 0;
        for (Enquete enquete : enquetes) {
            for (Long votosDaOpcao : enquete.calcularResultado().values()) {
                votos += votosDaOpcao;
            }
        }
        this.totalVotos = votos;
        this.mediaVotos = totalEnquete == 0 ? 0.0 : Math.round(votos * 10.0 / totalEnquete) / 10.0;
        this.dataGeracao = new Date();
    }

    // status: "ativa" ou "inativa" (campo "ativa" da enquete).
    public List<Enquete> filtrarStatus(String status) {
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

    // RF15 (PDF/Excel): será implementado com OpenPDF/Apache POI.
    public File exportar() {
        throw new UnsupportedOperationException("Exportação do relatório ainda não implementada (RF15)");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataGeracao() {
        return dataGeracao;
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

    public int getTotalEnquete() {
        return totalEnquete;
    }

    public long getTotalVotos() {
        return totalVotos;
    }

    public double getMediaVotos() {
        return mediaVotos;
    }

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }

    public List<Enquete> getEnquetes() {
        return enquetes;
    }

    @Override
    public String toString() {
        return "Relatorio{" +
                "id=" + id +
                ", escolaId=" + (escola != null ? escola.getId() : null) +
                ", dataGeracao=" + dataGeracao +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", totalEnquete=" + totalEnquete +
                ", totalVotos=" + totalVotos +
                ", mediaVotos=" + mediaVotos +
                '}';
    }
}
