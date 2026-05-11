package com.financas.pessoais.controller;

import com.financas.pessoais.dto.UpdatePersonalInfoRequest;
import com.financas.pessoais.dto.UserProfileResponse;
import com.financas.pessoais.entity.User;
import com.financas.pessoais.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * PerfilFinanceiroController
 *
 * Controller responsável pelas rotas de perfil do usuário.
 *
 * Por enquanto estamos usando o e-mail como parâmetro.
 *
 * Exemplo:
 * GET /perfil/completo?email=diego@email.com
 *
 * Depois, quando o JWT real estiver pronto, podemos buscar o e-mail
 * diretamente do token, sem precisar mandar na URL.
 */
@RestController
@RequestMapping("/perfil")
public class PerfilFinanceiroController {

    private final UserService userService;

    /*
     * Injeção de dependência.
     */
    public PerfilFinanceiroController(UserService userService) {
        this.userService = userService;
    }

    /*
     * Busca o perfil completo do usuário.
     *
     * Exemplo de chamada:
     * GET http://localhost:8080/perfil/completo?email=diego@email.com
     */
    @GetMapping("/completo")
    public ResponseEntity<UserProfileResponse> getCompleteProfile(
            @RequestParam String email
    ) {
        UserProfileResponse perfil = userService.getCompleteProfile(email);

        return ResponseEntity.ok(perfil);
    }

    /*
     * Atualiza dados pessoais, profissionais e endereço do usuário.
     *
     * Exemplo de chamada:
     * PUT http://localhost:8080/perfil/dados-pessoais?email=diego@email.com
     */
    @PutMapping("/dados-pessoais")
    public ResponseEntity<UserProfileResponse> updatePersonalInfo(
            @RequestParam String email,
            @RequestBody UpdatePersonalInfoRequest request
    ) {
        UserProfileResponse perfilAtualizado = userService.updatePersonalInfo(email, request);

        return ResponseEntity.ok(perfilAtualizado);
    }

    /*
     * Busca apenas a entidade User pelo e-mail.
     *
     * Essa rota é útil para teste.
     *
     * Exemplo:
     * GET http://localhost:8080/perfil/usuario?email=diego@email.com
     */
    @GetMapping("/usuario")
    public ResponseEntity<User> findUserByEmail(
            @RequestParam String email
    ) {
        User usuario = userService.findByEmail(email);

        return ResponseEntity.ok(usuario);
    }

    /*
     * Rota simples para testar se o controller está funcionando.
     *
     * Exemplo:
     * GET http://localhost:8080/perfil/status
     */
    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("PerfilFinanceiroController funcionando");
    }
}