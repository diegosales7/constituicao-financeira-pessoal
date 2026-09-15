package com.financas.pessoais.service;

import com.financas.pessoais.dto.PjAllocateResponse;
import com.financas.pessoais.entity.Empresa;
import com.financas.pessoais.repository.EmpresaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class PjTreasuryService {

    private final EmpresaRepository empresaRepository;

    public PjTreasuryService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    /**
     * Regra simples de tesouraria corporativa:
     * - 50% para operações (fluxo de caixa operacional)
     * - 30% para reservas (contingência/caixa)
     * - 20% para investimentos
     */
    public PjAllocateResponse allocate(Long empresaId, BigDecimal amount) {
        // Se empresaId for fornecido, apenas verifica existência; caso contrário, calcula alocação sem persistência.
        if (empresaId != null) {
            empresaRepository.findById(empresaId).orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada: " + empresaId));
        }

        BigDecimal operating = amount.multiply(new BigDecimal("0.5"));
        BigDecimal reserves = amount.multiply(new BigDecimal("0.3"));
        BigDecimal investments = amount.subtract(operating).subtract(reserves);

        // Ajuste de escala
        operating = operating.setScale(2, RoundingMode.HALF_UP);
        reserves = reserves.setScale(2, RoundingMode.HALF_UP);
        investments = investments.setScale(2, RoundingMode.HALF_UP);

        return new PjAllocateResponse(operating, reserves, investments);
    }
}
