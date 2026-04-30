package com.atlas;

import com.atlas.model.InventoryModel;
import com.atlas.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
class InventoryRepositoryIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        // Important: Create tables automatically for tests
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        
        // Prevent errors connecting to Eureka/Kafka during repo test
        registry.add("eureka.client.enabled", () -> "false");
    }

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    void shouldFindStockByProductId() {
        // Arrange: create and save a new inventory item
        InventoryModel item = new InventoryModel();
        item.setProductId("IPHONE-15");
        item.setQuantity(10);
        inventoryRepository.save(item);

        // Act: fetch the item by product ID
        Optional<InventoryModel> result = inventoryRepository.findByProductIdIgnoreCase("IPHONE-15");
        
        // Assert: verify it was saved and retrieved correctly
        assertTrue(result.isPresent());
        assertEquals(10, result.get().getQuantity());
    }
}
