package com.financas.pessoais.repository;

import com.financas.pessoais.entity.FinancialProfile;
import com.financas.pessoais.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinancialProfileRepository extends JpaRepository<FinancialProfile, Long> {
    Optional<FinancialProfile> findByUser(User user);
}
