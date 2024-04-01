package com.vmdev.eshop.dto;

import lombok.Value;

@Value
public class OrderProductReadDto {
    Long id;
    ProductReadDto product;
    Integer quantity;
}
