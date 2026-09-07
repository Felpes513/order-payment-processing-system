package com.inventoryservice.inventory_service.inventory.repository;

import com.inventoryservice.inventory_service.inventory.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}