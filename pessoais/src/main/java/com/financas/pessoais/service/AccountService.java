package com.financas.pessoais.service;

import com.financas.pessoais.model.Account;
import com.financas.pessoais.entity.User;
import com.financas.pessoais.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;

    private User getLoggedUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userService.findByEmail(email);
    }

    public Account create(Account account) {
        account.setUser(getLoggedUser());
        return accountRepository.save(account);
    }

    public List<Account> list() {
        return accountRepository.findByUser(getLoggedUser());
    }
}
