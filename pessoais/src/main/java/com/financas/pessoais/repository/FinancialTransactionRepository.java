package com.financas.pessoais.repository;

import com.financas.pessoais.entity.FinancialTransaction;
import com.financas.pessoais.entity.User;
import com.financas.pessoais.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Long> {

    List<FinancialTransaction> findByUserOrderByTransactionDateDesc(User user);

    List<FinancialTransaction> findByUserAndTypeOrderByTransactionDateDesc(
            User user,
            TransactionType type
    );

    List<FinancialTransaction> findByUserAndTransactionDateBetweenOrderByTransactionDateDesc(
            User user,
            LocalDateTime start,
            LocalDateTime end
    );
}