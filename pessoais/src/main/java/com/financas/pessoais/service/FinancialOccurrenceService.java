package com.financas.pessoais.service;

import com.financas.pessoais.dto.CreateOccurrenceRequest;
import com.financas.pessoais.dto.OccurrenceResponse;
import com.financas.pessoais.entity.FinancialOccurrence;
import com.financas.pessoais.entity.User;
import com.financas.pessoais.repository.FinancialOccurrenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialOccurrenceService {

    private final FinancialOccurrenceRepository occurrenceRepository;

    public OccurrenceResponse create(User user, CreateOccurrenceRequest request) {

        FinancialOccurrence occurrence = FinancialOccurrence.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .amount(request.getAmount())
                .correctionPercent(request.getCorrectionPercent())
                .occurrenceDate(request.getOccurrenceDate())
                .build();

        return toResponse(occurrenceRepository.save(occurrence));
    }

    public List<OccurrenceResponse> listAll(User user) {
        return occurrenceRepository.findByUserOrderByOccurrenceDateDesc(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private OccurrenceResponse toResponse(FinancialOccurrence occurrence) {
        return OccurrenceResponse.builder()
                .id(occurrence.getId())
                .title(occurrence.getTitle())
                .description(occurrence.getDescription())
                .amount(occurrence.getAmount())
                .correctionPercent(occurrence.getCorrectionPercent())
                .status(occurrence.getStatus())
                .occurrenceDate(occurrence.getOccurrenceDate())
                .build();
    }
}