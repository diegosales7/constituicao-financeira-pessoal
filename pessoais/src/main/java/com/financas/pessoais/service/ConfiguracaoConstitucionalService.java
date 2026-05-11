package com.financas.pessoais.service;

import com.financas.pessoais.dto.ConfiguracaoConstitucionalRequest;
import com.financas.pessoais.entity.ConfiguracaoConstitucional;
import com.financas.pessoais.entity.User;
import com.financas.pessoais.repository.ConfiguracaoConstitucionalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/*
 * Service responsável por buscar e atualizar
 * as regras constitucionais de cada usuário.
 */
@Service
public class ConfiguracaoConstitucionalService {

    private final ConfiguracaoConstitucionalRepository repository;
    private final UserService userService;

    public ConfiguracaoConstitucionalService(
            ConfiguracaoConstitucionalRepository repository,
            UserService userService
    ) {
        this.repository = repository;
        this.userService = userService;
    }

    public ConfiguracaoConstitucional buscarOuCriar(String email) {
        User user = userService.findByEmail(email);

        return repository.findByUser(user)
                .orElseGet(() -> {
                    ConfiguracaoConstitucional config = new ConfiguracaoConstitucional();
                    config.setUser(user);
                    return repository.save(config);
                });
    }

    public ConfiguracaoConstitucional atualizar(String email, ConfiguracaoConstitucionalRequest request) {
        ConfiguracaoConstitucional config = buscarOuCriar(email);

        LocalDate hoje = LocalDate.now();

        /*
         * Regra constitucional:
         * Só permite alterar na data da próxima revisão ou depois dela.
         */
        if (config.getProximaRevisao() != null && hoje.isBefore(config.getProximaRevisao())) {
            throw new RuntimeException(
                    "A Constituição só poderá ser alterada a partir da próxima revisão: "
                            + config.getProximaRevisao()
            );
        }

        validarSoma("Normal", request.getNormalInvestimento(), request.getNormalReservaFlex(), request.getNormalVida());
        validarSoma("Gatilho 1", request.getGatilho1Investimento(), request.getGatilho1ReservaFlex(), request.getGatilho1Vida());
        validarSoma("Gatilho 2", request.getGatilho2Investimento(), request.getGatilho2ReservaFlex(), request.getGatilho2Vida());

        config.setNormalInvestimento(request.getNormalInvestimento());
        config.setNormalReservaFlex(request.getNormalReservaFlex());
        config.setNormalVida(request.getNormalVida());

        config.setGatilho1Investimento(request.getGatilho1Investimento());
        config.setGatilho1ReservaFlex(request.getGatilho1ReservaFlex());
        config.setGatilho1Vida(request.getGatilho1Vida());

        config.setGatilho2Investimento(request.getGatilho2Investimento());
        config.setGatilho2ReservaFlex(request.getGatilho2ReservaFlex());
        config.setGatilho2Vida(request.getGatilho2Vida());

        if (request.getProximaRevisao() != null) {
            config.setProximaRevisao(request.getProximaRevisao());
        }

        return repository.save(config);
    }

    private void validarSoma(String nome, Integer investimento, Integer reserva, Integer vida) {
        if (investimento == null || reserva == null || vida == null) {
            throw new RuntimeException("Todos os percentuais de " + nome + " devem ser informados.");
        }

        int total = investimento + reserva + vida;

        if (total != 100) {
            throw new RuntimeException(
                    "A soma dos percentuais de " + nome + " deve ser 100%. Soma atual: " + total + "%"
            );
        }
    }
}