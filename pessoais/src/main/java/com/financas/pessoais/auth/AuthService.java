package com.financas.pessoais.auth;

import com.financas.pessoais.dto.AuthResponse;
import com.financas.pessoais.dto.LoginRequest;
import com.financas.pessoais.dto.RegisterRequest;
import com.financas.pessoais.entity.User;
import com.financas.pessoais.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

/*
 * AuthService
 *
 * Classe responsável pelas regras de autenticação:
 * - cadastro de usuário
 * - login de usuário
 *
 * IMPORTANTE:
 * Este projeto está usando a entidade:
 * com.financas.pessoais.entity.User
 *
 * Por isso NÃO usamos mais:
 * com.financas.pessoais.model.Usuario
 */
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    /*
     * Injeção de dependência.
     * O Spring entrega automaticamente o repository aqui.
     */
    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /*
     * Cadastro de usuário.
     *
     * Recebe os dados do RegisterRequest, cria um User
     * e salva no banco de dados.
     */
    public AuthResponse register(RegisterRequest request) {

        /*
         * Verifica se já existe um usuário com o mesmo e-mail.
         */
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado");
        }

        /*
         * Cria uma nova entidade User.
         */
        User usuario = new User();

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(request.getSenha());
        usuario.setIdade(request.getIdade());

        /*
         * Salva no banco.
         */
        usuarioRepository.save(usuario);

        /*
         * Token temporário.
         *
         * Como seu AuthController espera um token para colocar no cookie,
         * deixamos um token simples por enquanto.
         *
         * Depois podemos trocar por JWT real.
         */
        String token = "token-temporario-" + usuario.getId();

        /*
         * Retorna os dados principais para o front-end.
         */
        return new AuthResponse(
                token,
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getIdade()
        );
    }

    /*
     * Login de usuário.
     *
     * Busca o usuário por e-mail e confere a senha.
     */
    public AuthResponse login(LoginRequest request) {

        User usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("E-mail ou senha inválidos"));

        /*
         * Comparação simples de senha.
         *
         * Por enquanto está assim para o projeto funcionar.
         * Depois o ideal é usar BCrypt/PasswordEncoder.
         */
        if (!usuario.getSenha().equals(request.getSenha())) {
            throw new RuntimeException("E-mail ou senha inválidos");
        }

        String token = "token-temporario-" + usuario.getId();

        return new AuthResponse(
                token,
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getIdade()
        );
    }
}