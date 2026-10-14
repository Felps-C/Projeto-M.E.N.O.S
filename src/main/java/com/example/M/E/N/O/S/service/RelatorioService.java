
        package com.example.M.E.N.O.S.service;

import com.example.M.E.N.O.S.model.Enquete;
import com.example.M.E.N.O.S.model.Relatorio;
import com.example.M.E.N.O.S.repository.RelatorioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioService {

    private final RelatorioRepository relatorioRepository;

    public RelatorioService(RelatorioRepository relatorioRepository) {
        this.relatorioRepository = relatorioRepository;
    }

    public List<Relatorio> listarTodos() {
        return relatorioRepository.findAll();
    }

    public Relatorio buscarPorId(Long id) {
        return relatorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Relatório não encontrado com id: " + id));
    }

    public Relatorio salvar(Relatorio relatorio) {
        return relatorioRepository.save(relatorio);
    }

    public Relatorio atualizar(Long id, Relatorio dadosAtualizados) {
        Relatorio relatorio = buscarPorId(id);

        relatorio.setDataInicio(dadosAtualizados.getDataInicio());
        relatorio.setDataFim(dadosAtualizados.getDataFim());
        relatorio.setTotalEnquete(dadosAtualizados.getTotalEnquete());
        relatorio.setMediaVotos(dadosAtualizados.getMediaVotos());
        relatorio.setEnquetes(dadosAtualizados.getEnquetes());

        return relatorioRepository.save(relatorio);
    }

    public void deletar(Long id) {
        Relatorio relatorio = buscarPorId(id);
        relatorioRepository.delete(relatorio);
    }

    public Relatorio compilarDados(List<Enquete> enquetes) {
        Relatorio relatorio = new Relatorio();

        relatorio.CompilarDados(enquetes);

        return relatorioRepository.save(relatorio);
    }

    public List<Enquete> filtrarStatus(Long id, String status) {
        Relatorio relatorio = buscarPorId(id);

        if (!status.equalsIgnoreCase("ativa")
                && !status.equalsIgnoreCase("inativa")) {
            throw new IllegalArgumentException(
                    "Status inválido. Use 'ativa' ou 'inativa'.");
        }

        return relatorio.FiltrarStatus(status);
    }
}