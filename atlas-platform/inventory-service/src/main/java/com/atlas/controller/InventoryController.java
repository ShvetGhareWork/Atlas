package com.atlas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atlas.model.InventoryModel;
import com.atlas.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryModel> addInventory(@RequestBody InventoryModel inventory)
    {
        InventoryModel inventoryModel = this.inventoryService.addInventory(inventory);
        return ResponseEntity.ok(inventoryModel);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryModel> getInventoryById(@PathVariable String productId){
        InventoryModel inventoryModel = this.inventoryService.checkStock(productId);
        return ResponseEntity.ok(inventoryModel);            
    }
    
}