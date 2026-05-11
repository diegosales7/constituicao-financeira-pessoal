package com.financas.pessoais.controller;

import com.financas.pessoais.auth.AuthService;
import com.financas.pessoais.dto.AuthResponse;
import com.financas.pessoais.dto.LoginRequest;
import com.financas.pessoais.dto.RegisterRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request,
            HttpServletResponse response
    ) {
        AuthResponse authResponse = service.register(request);

        addJwtToCookie(authResponse.getToken(), response);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        AuthResponse authResponse = service.login(request);

        addJwtToCookie(authResponse.getToken(), response);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true em produção (HTTPS)
        cookie.setPath("/");
        cookie.setMaxAge(0); // remove o cookie

        response.addCookie(cookie);

        return ResponseEntity.ok("Logout realizado com sucesso");
    }

    // 🔹 Método auxiliar
    private void addJwtToCookie(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true em produção
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 1 dia

        response.addCookie(cookie);
    }
}