package com.financas.pessoais.controller;

import com.financas.pessoais.dto.FinancialAnalysisResponse;
import com.financas.pessoais.entity.User;
import com.financas.pessoais.service.FinancialAnalysisService;
import com.financas.pessoais.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analise")
@RequiredArgsConstructor
public class FinancialAnalysisController {

    private final FinancialAnalysisService analysisService;
    private final UserService userService;

    /**
     * Obtém a análise de saúde financeira do usuário
     */
    @GetMapping("/saude-financeira")
    public ResponseEntity<FinancialAnalysisResponse> getFinancialAnalysis(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User user = userService.findByEmail(userDetails.getUsername());
        FinancialAnalysisResponse analysis = analysisService.analyzeFinancialHealth(user);
        
        return ResponseEntity.ok(analysis);
    }
}

