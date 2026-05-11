package com.financas.pessoais.service;

import com.financas.pessoais.dto.CreateTransactionRequest;
import com.financas.pessoais.dto.TransactionResponse;
import com.financas.pessoais.entity.FinancialTransaction;
import com.financas.pessoais.entity.User;
import com.financas.pessoais.repository.FinancialTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialTransactionService {

    private final FinancialTransactionRepository transactionRepository;

    public TransactionResponse create(User user, CreateTransactionRequest request) {

        FinancialTransaction transaction = FinancialTransaction.builder()
                .user(user)
                .type(request.getType())
                .description(request.getDescription())
                .category(request.getCategory())
                .amount(request.getAmount())
                .transactionDate(request.getTransactionDate())
                .affectsConstitution(request.getAffectsConstitution())
                .notes(request.getNotes())
                .build();

        FinancialTransaction saved = transactionRepository.save(transaction);

        return toResponse(saved);
    }

    public List<TransactionResponse> listAll(User user) {
        return transactionRepository.findByUserOrderByTransactionDateDesc(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(User user, Long id) {
        FinancialTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Você não tem permissão para excluir esta transação");
        }

        transactionRepository.delete(transaction);
    }

    private TransactionResponse toResponse(FinancialTransaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .description(transaction.getDescription())
                .category(transaction.getCategory())
                .amount(transaction.getAmount())
                .transactionDate(transaction.getTransactionDate())
                .affectsConstitution(transaction.getAffectsConstitution())
                .notes(transaction.getNotes())
                .build();
    }
}