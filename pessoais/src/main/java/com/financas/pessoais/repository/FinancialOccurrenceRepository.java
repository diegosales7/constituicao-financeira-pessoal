package com.financas.pessoais.repository;

import com.financas.pessoais.entity.FinancialOccurrence;
import com.financas.pessoais.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialOccurrenceRepository extends JpaRepository<FinancialOccurrence, Long> {

    List<FinancialOccurrence> findByUserOrderByOccurrenceDateDesc(User user);
}