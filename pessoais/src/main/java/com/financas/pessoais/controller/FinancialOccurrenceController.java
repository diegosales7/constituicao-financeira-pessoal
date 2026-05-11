package com.financas.pessoais.controller;

import com.financas.pessoais.dto.CreateOccurrenceRequest;
import com.financas.pessoais.dto.OccurrenceResponse;
import com.financas.pessoais.entity.User;
import com.financas.pessoais.service.FinancialOccurrenceService;
import com.financas.pessoais.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ocorrencias")
@RequiredArgsConstructor
public class FinancialOccurrenceController {

    private final FinancialOccurrenceService occurrenceService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<OccurrenceResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CreateOccurrenceRequest request
    ) {
        User user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(occurrenceService.create(user, request));
    }

    @GetMapping
    public ResponseEntity<List<OccurrenceResponse>> listAll(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(occurrenceService.listAll(user));
    }
}