package com.financas.pessoais.controller;

import com.financas.pessoais.dto.CreateAssetRequest;
import com.financas.pessoais.dto.AssetResponse;
import com.financas.pessoais.entity.User;
import com.financas.pessoais.service.AssetService;
import com.financas.pessoais.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ativos")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;
    private final UserService userService;

    @PostMapping("/criar")
    public ResponseEntity<AssetResponse> createAsset(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CreateAssetRequest request) {
        User user = userService.findByEmail(userDetails.getUsername());
        AssetResponse response = assetService.createAsset(user, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/meus-ativos")
    public ResponseEntity<List<AssetResponse>> getMyAssets(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<AssetResponse> assets = assetService.getAssetsByUser(user);
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/por-categoria")
    public ResponseEntity<List<AssetResponse>> getAssetsByCategory(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String category) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<AssetResponse> assets = assetService.getAssetsByCategory(user, category);
        return ResponseEntity.ok(assets);
    }

    @PutMapping("/atualizar/{assetId}")
    public ResponseEntity<AssetResponse> updateAsset(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long assetId,
            @RequestBody CreateAssetRequest request) {
        User user = userService.findByEmail(userDetails.getUsername());
        AssetResponse response = assetService.updateAsset(user, assetId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deletar/{assetId}")
    public ResponseEntity<Void> deleteAsset(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long assetId) {
        User user = userService.findByEmail(userDetails.getUsername());
        assetService.deleteAsset(user, assetId);
        return ResponseEntity.noContent().build();
    }
}

