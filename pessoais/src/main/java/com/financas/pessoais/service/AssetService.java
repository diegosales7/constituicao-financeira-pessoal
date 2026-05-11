package com.financas.pessoais.service;

import com.financas.pessoais.dto.CreateAssetRequest;
import com.financas.pessoais.dto.AssetResponse;
import com.financas.pessoais.entity.Asset;
import com.financas.pessoais.entity.User;
import com.financas.pessoais.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetResponse createAsset(User user, CreateAssetRequest request) {
        Asset asset = new Asset();
        asset.setUser(user);
        asset.setCategory(request.getCategory());
        asset.setSubcategory(request.getSubcategory());
        asset.setInvestmentValue(request.getInvestmentValue());
        asset.setInvestmentDate(request.getInvestmentDate());
        asset.setDescription(request.getDescription());
        asset.setCreatedAt(LocalDateTime.now());
        asset.setUpdatedAt(LocalDateTime.now());

        Asset savedAsset = assetRepository.save(asset);
        return convertToResponse(savedAsset);
    }

    public List<AssetResponse> getAssetsByUser(User user) {
        return assetRepository.findByUser(user)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<AssetResponse> getAssetsByCategory(User user, String category) {
        return assetRepository.findByUserAndCategory(user, category)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public AssetResponse updateAsset(User user, Long assetId, CreateAssetRequest request) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Ativo não encontrado"));

        if (!asset.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Acesso negado");
        }

        asset.setCategory(request.getCategory());
        asset.setSubcategory(request.getSubcategory());
        asset.setInvestmentValue(request.getInvestmentValue());
        asset.setInvestmentDate(request.getInvestmentDate());
        asset.setDescription(request.getDescription());
        asset.setUpdatedAt(LocalDateTime.now());

        Asset updatedAsset = assetRepository.save(asset);
        return convertToResponse(updatedAsset);
    }

    public void deleteAsset(User user, Long assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Ativo não encontrado"));

        if (!asset.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Acesso negado");
        }

        assetRepository.deleteById(assetId);
    }

    private AssetResponse convertToResponse(Asset asset) {
        return new AssetResponse(
                asset.getId(),
                asset.getCategory(),
                asset.getSubcategory(),
                asset.getInvestmentValue(),
                asset.getInvestmentDate(),
                asset.getDescription(),
                asset.getCreatedAt(),
                asset.getUpdatedAt()
        );
    }
}

