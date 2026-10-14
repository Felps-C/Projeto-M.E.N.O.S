package com.example.M.E.N.O.S.repository;

import com.example.M.E.N.O.S.model.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    List<Movimentacao> findByProdutoIdInOrderByDataHoraDesc(Collection<Long> produtoIds);
}
