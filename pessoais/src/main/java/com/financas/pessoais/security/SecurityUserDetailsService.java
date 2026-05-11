package com.financas.pessoais.security;

import com.financas.pessoais.entity.User;
import com.financas.pessoais.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
 * SecurityUserDetailsService
 *
 * Essa classe é usada pelo Spring Security para carregar
 * o usuário pelo e-mail durante o processo de autenticação.
 */
@Service
public class SecurityUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public SecurityUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /*
     * O Spring Security chama esse método quando precisa
     * buscar um usuário pelo e-mail.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        /*
         * IMPORTANTE:
         * user.getRole() já retorna String.
         * Por isso NÃO usamos .name().
         */
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}