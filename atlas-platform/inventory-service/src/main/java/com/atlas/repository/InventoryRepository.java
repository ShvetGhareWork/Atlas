package com.atlas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atlas.model.InventoryModel;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryModel, String> {
    java.util.Optional<InventoryModel> findByProductIdIgnoreCase(String productId);
}
