package com.financas.pessoais.controller;

import com.financas.pessoais.dto.CreateTransacaoRequest;
import com.financas.pessoais.dto.TransacaoResponse;
import com.financas.pessoais.entity.CategoriaTransacao;
import com.financas.pessoais.entity.TipoTransacao;
import com.financas.pessoais.entity.User;
import com.financas.pessoais.service.TransacaoService;
import com.financas.pessoais.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<TransacaoResponse> criar(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CreateTransacaoRequest request
    ) {
        User user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(transacaoService.criar(user, request));
    }

    @GetMapping
    public ResponseEntity<List<TransacaoResponse>> listar(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(transacaoService.listarTodas(user));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<TransacaoResponse>> listarPorTipo(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable TipoTransacao tipo
    ) {
        User user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(transacaoService.listarPorTipo(user, tipo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        User user = userService.findByEmail(userDetails.getUsername());
        transacaoService.deletar(user, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categorias/receitas")
    public ResponseEntity<List<Map<String, String>>> getCategoriasReceitas() {
        return ResponseEntity.ok(
            Arrays.stream(CategoriaTransacao.getReceitas())
                .map(categoria -> Map.of(
                    "value", categoria.name(),
                    "label", categoria.getDescricao()
                ))
                .toList()
        );
    }

    @GetMapping("/categorias/despesas")
    public ResponseEntity<List<Map<String, String>>> getCategoriasDespesas() {
        return ResponseEntity.ok(
            Arrays.stream(CategoriaTransacao.getDespesas())
                .map(categoria -> Map.of(
                    "value", categoria.name(),
                    "label", categoria.getDescricao()
                ))
                .toList()
        );
    }
}