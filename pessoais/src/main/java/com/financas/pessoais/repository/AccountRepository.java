package com.financas.pessoais.repository;

import com.financas.pessoais.model.Account;
import com.financas.pessoais.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByUser(User user);
}
