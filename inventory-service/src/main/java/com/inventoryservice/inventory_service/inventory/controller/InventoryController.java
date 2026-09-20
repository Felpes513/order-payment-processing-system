package com.inventoryservice.inventory_service.inventory.controller;

import com.inventoryservice.inventory_service.inventory.dto.CreateProductRequest;
import com.inventoryservice.inventory_service.inventory.dto.ProductResponse;
import com.inventoryservice.inventory_service.inventory.dto.ReserveProductRequest;
import com.inventoryservice.inventory_service.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@Valid @RequestBody CreateProductRequest request) {
        return inventoryService.createProduct(request);
    }

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable UUID id) {
        return inventoryService.findById(id);
    }

    @PatchMapping("/{id}/reserve")
    public ProductResponse reserveProduct(@PathVariable UUID id, @Valid @RequestBody ReserveProductRequest request) {
        return inventoryService.reserveProduct(id, request);
    }
}