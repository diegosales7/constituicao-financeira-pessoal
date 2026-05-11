package com.financas.pessoais.controller;

import com.financas.pessoais.model.Account;
import com.financas.pessoais.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @PostMapping
    public Account create(@RequestBody Account account) {
        return service.create(account);
    }

    @GetMapping
    public List<Account> list() {
        return service.list();
    }
}
