package com.financas.pessoais.repository;

import com.financas.pessoais.entity.Asset;
import com.financas.pessoais.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByUser(User user);
    List<Asset> findByUserAndCategory(User user, String category);
}

