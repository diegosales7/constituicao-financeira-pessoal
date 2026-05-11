package com.financas.pessoais.repository;

import com.financas.pessoais.entity.TipoTransacao;
import com.financas.pessoais.entity.Transacao;
import com.financas.pessoais.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByUserOrderByDataDesc(User user);

    List<Transacao> findByUserAndTipoOrderByDataDesc(User user, TipoTransacao tipo);
}