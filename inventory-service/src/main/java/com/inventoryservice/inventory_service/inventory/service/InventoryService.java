package com.inventoryservice.inventory_service.inventory.service;

import com.inventoryservice.inventory_service.inventory.domain.Product;
import com.inventoryservice.inventory_service.inventory.dto.CreateProductRequest;
import com.inventoryservice.inventory_service.inventory.dto.ProductResponse;
import com.inventoryservice.inventory_service.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = Product.create(
                request.name(),
                request.availableQuantity()
        );

        Product savedProduct = productRepository.save(product);

        return ProductResponse.fromDomain(savedProduct);
    }
}