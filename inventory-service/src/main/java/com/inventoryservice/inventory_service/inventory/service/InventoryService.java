package com.inventoryservice.inventory_service.inventory.service;

import com.inventoryservice.inventory_service.inventory.domain.Product;
import com.inventoryservice.inventory_service.inventory.dto.CreateProductRequest;
import com.inventoryservice.inventory_service.inventory.dto.ProductResponse;
import com.inventoryservice.inventory_service.inventory.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.inventoryservice.inventory_service.shared.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

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

    @Transactional(readOnly = true)
    public ProductResponse findById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return ProductResponse.fromDomain(product);
    }
}