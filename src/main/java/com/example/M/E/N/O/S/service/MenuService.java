package com.example.M.E.N.O.S.service;

import com.example.M.E.N.O.S.dto.MenuRequest;
import com.example.M.E.N.O.S.model.Escola;
import com.example.M.E.N.O.S.model.Menu;
import com.example.M.E.N.O.S.model.Merenda;
import com.example.M.E.N.O.S.repository.EscolaRepository;
import com.example.M.E.N.O.S.repository.MenuRepository;
import com.example.M.E.N.O.S.repository.MerendaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final EscolaRepository escolaRepository;
    private final MerendaRepository merendaRepository;

    // Injeção de dependência via construtor
    public MenuService(MenuRepository menuRepository,
                       EscolaRepository escolaRepository,
                       MerendaRepository merendaRepository) {
        this.menuRepository = menuRepository;
        this.escolaRepository = escolaRepository;
        this.merendaRepository = merendaRepository;
    }

    @Transactional
    public Menu criar(Long escolaId, MenuRequest request) {
        Escola escola = escolaRepository.findById(escolaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Escola não encontrada com id: " + escolaId));
        Menu menu = new Menu();
        menu.setEscola(escola);
        aplicar(menu, request);
        return menuRepository.save(menu);
    }

    // Lista os cardápios de uma escola. Com ?ativa=true devolve só os publicados
    // (é o que o aluno enxerga).
    @Transactional(readOnly = true)
    public List<Menu> listarPorEscola(Long escolaId, Boolean ativa) {
        if (!escolaRepository.existsById(escolaId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Escola não encontrada com id: " + escolaId);
        }
        if (ativa == null) {
            return menuRepository.findByEscolaIdOrderByDataInicioDesc(escolaId);
        }
        return menuRepository.findByEscolaIdAndAtivaOrderByDataInicioDesc(escolaId, ativa);
    }

    @Transactional(readOnly = true)
    public Menu buscarPorId(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cardápio não encontrado com id: " + id));
    }

    // Substitui os dados do cardápio, inclusive a lista de merendas.
    @Transactional
    public Menu atualizar(Long id, MenuRequest request) {
        Menu menu = buscarPorId(id);
        aplicar(menu, request);
        return menuRepository.save(menu);
    }

    @Transactional
    public Menu adicionarMerenda(Long menuId, Long merendaId) {
        Menu menu = buscarPorId(menuId);
        Merenda merenda = merendaRepository.findById(merendaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Merenda não encontrada com id: " + merendaId));
        boolean jaEstaNoCardapio = menu.getMerendas().stream().anyMatch(m -> m.getId().equals(merendaId));
        if (!jaEstaNoCardapio) {
            menu.getMerendas().add(merenda);
        }
        return menu;
    }

    @Transactional
    public Menu removerMerenda(Long menuId, Long merendaId) {
        Menu menu = buscarPorId(menuId);
        boolean removida = menu.getMerendas().removeIf(m -> m.getId().equals(merendaId));
        if (!removida) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "A merenda " + merendaId + " não está neste cardápio");
        }
        if (Boolean.TRUE.equals(menu.getAtiva()) && menu.getMerendas().isEmpty()) {
            // O erro desfaz a remoção (a transação é cancelada).
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Um cardápio ativo precisa ter pelo menos uma merenda");
        }
        return menu;
    }

    @Transactional
    public void deletar(Long id) {
        Menu menu = buscarPorId(id);
        try {
            menuRepository.delete(menu);
            menuRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "O cardápio possui feedbacks vinculados e não pode ser excluído");
        }
    }

    // ---------- auxiliares ----------

    private void aplicar(Menu menu, MenuRequest request) {
        List<Merenda> merendas = buscarMerendas(request.merendaIds());
        boolean ativa = request.ativa() == null || request.ativa();
        if (ativa && merendas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Um cardápio ativo precisa ter pelo menos uma merenda");
        }
        menu.setDataInicio(request.dataInicio());
        menu.setAtiva(ativa);
        menu.setMerendas(merendas);
    }

    private List<Merenda> buscarMerendas(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> distintos = ids.stream().distinct().toList();
        List<Merenda> encontradas = merendaRepository.findAllById(distintos);
        if (encontradas.size() != distintos.size()) {
            Set<Long> achados = encontradas.stream().map(Merenda::getId).collect(Collectors.toSet());
            List<Long> faltando = distintos.stream().filter(id -> !achados.contains(id)).toList();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Merenda(s) não encontrada(s): " + faltando);
        }
        return new ArrayList<>(encontradas);
    }
}
