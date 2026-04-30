package com.atlas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsDto {

    private String productId;
    @NotBlank(message = "SKU Code is required")
    private String skuCode;
    @NotNull(message = "Price is required")
    private BigDecimal price;
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}