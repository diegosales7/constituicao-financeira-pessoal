package com.financas.pessoais.service;

import com.financas.pessoais.dto.CreateTransacaoRequest;
import com.financas.pessoais.dto.TransacaoResponse;
import com.financas.pessoais.entity.TipoTransacao;
import com.financas.pessoais.entity.Transacao;
import com.financas.pessoais.entity.User;
import com.financas.pessoais.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository repository;

    public TransacaoResponse criar(User user, CreateTransacaoRequest request) {

        if (request.getValor() == null || request.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O valor da transação precisa ser maior que zero");
        }

        Transacao transacao = new Transacao();

        transacao.setUser(user);
        transacao.setTipo(request.getTipo());
        transacao.setCategoria(request.getCategoria());
        transacao.setValor(request.getValor());
        transacao.setData(request.getData());
        transacao.setDescricao(request.getDescricao());

        // Regra da Constituição:
        // Quando for RECEITA, calcula automaticamente sobre o valor recebido.
        // Não precisa informar renda mensal.
        if (request.getTipo() == TipoTransacao.RECEITA) {
            BigDecimal valor = request.getValor();

            BigDecimal investimento = calcularPercentual(valor, new BigDecimal("30"));
            BigDecimal reserva = calcularPercentual(valor, new BigDecimal("10"));
            BigDecimal vida = valor.subtract(investimento).subtract(reserva);

            transacao.setInvestimentoConstitucional(investimento);
            transacao.setReservaFlex(reserva);
            transacao.setValorVida(vida);
        } else {
            transacao.setInvestimentoConstitucional(BigDecimal.ZERO);
            transacao.setReservaFlex(BigDecimal.ZERO);
            transacao.setValorVida(BigDecimal.ZERO);
        }

        Transacao salva = repository.save(transacao);

        return converter(salva);
    }

    public List<TransacaoResponse> listarTodas(User user) {
        return repository.findByUserOrderByDataDesc(user)
                .stream()
                .map(this::converter)
                .toList();
    }

    public List<TransacaoResponse> listarPorTipo(User user, TipoTransacao tipo) {
        return repository.findByUserAndTipoOrderByDataDesc(user, tipo)
                .stream()
                .map(this::converter)
                .toList();
    }

    public void deletar(User user, Long id) {
        Transacao transacao = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        if (!transacao.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Acesso negado");
        }

        repository.delete(transacao);
    }

    private BigDecimal calcularPercentual(BigDecimal valor, BigDecimal percentual) {
        return valor.multiply(percentual)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    private TransacaoResponse converter(Transacao transacao) {
        return TransacaoResponse.builder()
                .id(transacao.getId())
                .tipo(transacao.getTipo())
                .categoria(transacao.getCategoria())
                .valor(transacao.getValor())
                .data(transacao.getData())
                .descricao(transacao.getDescricao())
                .investimentoConstitucional(transacao.getInvestimentoConstitucional())
                .reservaFlex(transacao.getReservaFlex())
                .valorVida(transacao.getValorVida())
                .build();
    }
}