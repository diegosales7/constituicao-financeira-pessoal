package com.financas.pessoais.service;

import com.financas.pessoais.dto.UpdatePersonalInfoRequest;
import com.financas.pessoais.dto.UserProfileResponse;
import com.financas.pessoais.entity.User;
import com.financas.pessoais.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

/*
 * UserService
 *
 * Classe responsável pelas regras relacionadas ao usuário.
 *
 * Ela será usada por:
 * - PerfilFinanceiroController
 * - TransacaoController
 * - outros controllers que precisem buscar o usuário logado
 */
@Service
public class UserService {

    private final UsuarioRepository usuarioRepository;

    /*
     * Injeção de dependência.
     */
    public UserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /*
     * Busca usuário por e-mail.
     *
     * IMPORTANTE:
     * Esse método retorna User, porque seus controllers esperam:
     * com.financas.pessoais.entity.User
     */
    public User findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    /*
     * Retorna o perfil completo do usuário.
     *
     * Este método retorna UserProfileResponse,
     * não retorna a entidade User diretamente.
     *
     * Isso corrige o erro:
     * User/Usuario cannot be converted to UserProfileResponse
     */
    public UserProfileResponse getCompleteProfile(String email) {
        User usuario = findByEmail(email);

        return new UserProfileResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getIdade(),
                usuario.getTelefone(),
                usuario.getCpf(),
                usuario.getDataNascimento(),
                usuario.getProfissao(),
                usuario.getEmpresa(),
                usuario.getCargo(),
                usuario.getRendaMensal(),
                usuario.getEndereco(),
                usuario.getCidade(),
                usuario.getEstado(),
                usuario.getCep()
        );
    }

    /*
     * Atualiza informações pessoais, profissionais e endereço.
     *
     * Esse método é chamado pelo PerfilFinanceiroController.
     */
    public UserProfileResponse updatePersonalInfo(String email, UpdatePersonalInfoRequest request) {
        User usuario = findByEmail(email);

        /*
         * Dados básicos
         */
        usuario.setNome(request.getNome());
        usuario.setIdade(request.getIdade());

        /*
         * Dados pessoais
         */
        usuario.setTelefone(request.getTelefone());
        usuario.setCpf(request.getCpf());
        usuario.setDataNascimento(request.getDataNascimento());

        /*
         * Dados profissionais
         */
        usuario.setProfissao(request.getProfissao());
        usuario.setEmpresa(request.getEmpresa());
        usuario.setCargo(request.getCargo());
        usuario.setRendaMensal(request.getRendaMensal());

        /*
         * Endereço
         */
        usuario.setEndereco(request.getEndereco());
        usuario.setCidade(request.getCidade());
        usuario.setEstado(request.getEstado());
        usuario.setCep(request.getCep());

        /*
         * Salva alterações no banco.
         */
        User usuarioAtualizado = usuarioRepository.save(usuario);

        /*
         * Retorna o perfil atualizado para o front-end.
         */
        return new UserProfileResponse(
                usuarioAtualizado.getId(),
                usuarioAtualizado.getNome(),
                usuarioAtualizado.getEmail(),
                usuarioAtualizado.getIdade(),
                usuarioAtualizado.getTelefone(),
                usuarioAtualizado.getCpf(),
                usuarioAtualizado.getDataNascimento(),
                usuarioAtualizado.getProfissao(),
                usuarioAtualizado.getEmpresa(),
                usuarioAtualizado.getCargo(),
                usuarioAtualizado.getRendaMensal(),
                usuarioAtualizado.getEndereco(),
                usuarioAtualizado.getCidade(),
                usuarioAtualizado.getEstado(),
                usuarioAtualizado.getCep()
        );
    }
}