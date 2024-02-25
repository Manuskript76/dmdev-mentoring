package com.vmdev.eshop.filter;

import com.vmdev.eshop.entity.enums.ProductType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductFilter {
    String name;

    Integer cost;

    ProductType type;

    String manufacturer;
}