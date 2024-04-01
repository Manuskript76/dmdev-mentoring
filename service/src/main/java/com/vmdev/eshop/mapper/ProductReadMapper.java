package com.vmdev.eshop.mapper;

import com.vmdev.eshop.dto.ProductReadDto;
import com.vmdev.eshop.dto.ReviewReadDto;
import com.vmdev.eshop.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ProductReadMapper implements Mapper<Product, ProductReadDto> {

    private final ReviewReadMapper reviewReadMapper;

    @Override
    public ProductReadDto map(Product object) {
        List<ReviewReadDto> reviews = Optional.of(object.getReviews().stream()
                        .map(reviewReadMapper::map)
                        .toList())
                .orElse(Collections.emptyList());

        return new ProductReadDto(
                object.getId(),
                object.getName(),
                object.getDescription(),
                object.getCost(),
                object.getQuantity(),
                object.getType(),
                object.getManufacturer(),
                reviews);
    }
}