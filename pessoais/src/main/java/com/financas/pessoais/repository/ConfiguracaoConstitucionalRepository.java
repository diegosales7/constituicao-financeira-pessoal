package com.financas.pessoais.repository;

import com.financas.pessoais.entity.ConfiguracaoConstitucional;
import com.financas.pessoais.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfiguracaoConstitucionalRepository extends JpaRepository<ConfiguracaoConstitucional, Long> {

    Optional<ConfiguracaoConstitucional> findByUser(User user);
}