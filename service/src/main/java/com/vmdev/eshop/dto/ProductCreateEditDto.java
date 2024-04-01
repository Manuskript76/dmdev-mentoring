package com.vmdev.eshop.dto;

import com.vmdev.eshop.entity.enums.ProductType;
import lombok.Value;

@Value
public class ProductCreateEditDto {
    String name;
    String description;
    Integer cost;
    Integer quantity;
    ProductType type;
    String manufacturer;
}
