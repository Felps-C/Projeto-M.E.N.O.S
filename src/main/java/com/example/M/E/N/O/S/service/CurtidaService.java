package com.example.M.E.N.O.S.service;

import com.example.M.E.N.O.S.dto.CurtidaRequest;
import com.example.M.E.N.O.S.dto.CurtidasMenu;
import com.example.M.E.N.O.S.model.Curtida;
import com.example.M.E.N.O.S.model.Estudante;
import com.example.M.E.N.O.S.model.Menu;
import com.example.M.E.N.O.S.repository.CurtidaRepository;
import com.example.M.E.N.O.S.repository.EstudanteRepository;
import com.example.M.E.N.O.S.repository.MenuRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class CurtidaService {

    private final CurtidaRepository curtidaRepository;
    private final EstudanteRepository estudanteRepository;
    private final MenuRepository menuRepository;

    // Injeção de dependência via construtor
    public CurtidaService(CurtidaRepository curtidaRepository,
                          EstudanteRepository estudanteRepository,
                          MenuRepository menuRepository) {
        this.curtidaRepository = curtidaRepository;
        this.estudanteRepository = estudanteRepository;
        this.menuRepository = menuRepository;
    }

    // O estudante só pode curtir cardápios da própria escola, e uma vez só por cardápio.
    @Transactional
    public Curtida curtir(CurtidaRequest request) {
        Estudante estudante = buscarEstudante(request.estudanteId());
        Menu menu = buscarMenu(request.menuId());

        if (!estudante.getEscola().getId().equals(menu.getEscola().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "O estudante não pertence à escola deste cardápio");
        }
        if (curtidaRepository.existsByEstudante_IdAndMenu_Id(estudante.getId(), menu.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O estudante já curtiu este cardápio");
        }

        Curtida curtida = new Curtida();
        curtida.setEstudante(estudante);
        curtida.setMenu(menu);
        curtida.setData(LocalDateTime.now());
        try {
            // saveAndFlush: se duas requisições chegarem juntas, a restrição única do banco
            // barra a segunda e ela vira 409 em vez de 500.
            return curtidaRepository.saveAndFlush(curtida);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O estudante já curtiu este cardápio");
        }
    }

    @Transactional
    public void descurtir(Long estudanteId, Long menuId) {
        Curtida curtida = curtidaRepository.findByEstudante_IdAndMenu_Id(estudanteId, menuId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "O estudante não curtiu este cardápio"));
        curtidaRepository.delete(curtida);
    }

    // Total de curtidas do cardápio; com estudanteId, diz também se esse estudante já curtiu.
    @Transactional(readOnly = true)
    public CurtidasMenu contar(Long menuId, Long estudanteId) {
        buscarMenu(menuId);
        Boolean curtiu = null;
        if (estudanteId != null) {
            buscarEstudante(estudanteId);
            curtiu = curtidaRepository.existsByEstudante_IdAndMenu_Id(estudanteId, menuId);
        }
        return new CurtidasMenu(menuId, curtidaRepository.countByMenu_Id(menuId), curtiu);
    }

    // ---------- auxiliares ----------

    private Estudante buscarEstudante(Long id) {
        return estudanteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Estudante não encontrado com id: " + id));
    }

    private Menu buscarMenu(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cardápio não encontrado com id: " + id));
    }
}
