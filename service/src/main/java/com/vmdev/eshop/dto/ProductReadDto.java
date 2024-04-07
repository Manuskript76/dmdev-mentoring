package com.vmdev.eshop.dto;

import com.vmdev.eshop.entity.enums.ProductType;
import lombok.Value;

import java.util.List;

@Value
public class ProductReadDto {
    Long id;
    String name;
    String description;
    Integer cost;
    Integer quantity;
    ProductType type;
    String manufacturer;
    String image;
    List<ReviewReadDto> reviews;
}