package com.vmdev.eshop.mapper;

import com.vmdev.eshop.dto.ProductReadDto;
import com.vmdev.eshop.dto.ReviewReadDto;
import com.vmdev.eshop.entity.Product;
import com.vmdev.eshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@RequiredArgsConstructor
@Component
public class ProductReadMapper implements Mapper<Product, ProductReadDto> {

    private final ReviewReadMapper reviewReadMapper;
    private final ProductRepository productRepository;

    @Override
    public ProductReadDto map(Product object) {
        List<ReviewReadDto> reviews = Optional.of(object.getReviews().stream()
                        .map(reviewReadMapper::map)
                        .toList())
                .orElse(emptyList());

        return new ProductReadDto(
                object.getId(),
                object.getName(),
                object.getDescription(),
                object.getCost(),
                object.getQuantity(),
                object.getType(),
                object.getManufacturer(),
                object.getImage(),
                reviews);
    }

    public Product map(ProductReadDto productReadDto) {
        Product product = productRepository.findById(productReadDto.getId()).orElseThrow();
        product.setId(productReadDto.getId());
        product.setCost(productReadDto.getCost());
        product.setType(productReadDto.getType());
        product.setName(productReadDto.getName());
        product.setDescription(productReadDto.getDescription());
        product.setManufacturer(productReadDto.getManufacturer());
        product.setImage(productReadDto.getImage());
        return product;
    }
}
