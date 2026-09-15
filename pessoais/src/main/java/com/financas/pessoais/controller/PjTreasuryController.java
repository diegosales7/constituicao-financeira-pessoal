package com.financas.pessoais.controller;

import com.financas.pessoais.dto.PjAllocateRequest;
import com.financas.pessoais.dto.PjAllocateResponse;
import com.financas.pessoais.service.PjTreasuryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/pj/treasury")
public class PjTreasuryController {

    private final PjTreasuryService treasuryService;

    public PjTreasuryController(PjTreasuryService treasuryService) {
        this.treasuryService = treasuryService;
    }

    @PostMapping("/allocate")
    public ResponseEntity<PjAllocateResponse> allocate(@RequestBody PjAllocateRequest request) {
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().build();
        }

        PjAllocateResponse resp = treasuryService.allocate(request.getEmpresaId(), request.getAmount());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/allocate-by-query")
    public ResponseEntity<PjAllocateResponse> allocateByQuery(@RequestParam(required = false) Long empresaId, @RequestParam BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().build();
        }
        PjAllocateResponse resp = treasuryService.allocate(empresaId, amount);
        return ResponseEntity.ok(resp);
    }
}

