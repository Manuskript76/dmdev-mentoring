package com.vmdev.eshop.dto;

import lombok.Value;

@Value
public class OrderProductDto {
    Long id;
    ProductReadDto product;
    Integer quantity;
}
