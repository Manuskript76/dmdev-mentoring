package com.vmdev.eshop.service;

import com.vmdev.eshop.dto.ProductCreateEditDto;
import com.vmdev.eshop.dto.ProductReadDto;
import com.vmdev.eshop.mapper.ProductCreateEditMapper;
import com.vmdev.eshop.mapper.ProductReadMapper;
import com.vmdev.eshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductReadMapper productReadMapper;
    private final ProductCreateEditMapper productCreateEditMapper;

    public List<ProductReadDto> findAll() {
        return productRepository.findAll().stream()
                .map(productReadMapper::map)
                .toList();
    }

    // TODO: 31.03.2024 добавить фильтр 

    public Optional<ProductReadDto> findById(Long id) {
        return productRepository.findById(id)
                .map(productReadMapper::map);
    }

    @Transactional
    public ProductReadDto create(ProductCreateEditDto productDto) {
        return Optional.of(productDto)
                .map(productCreateEditMapper::map)
                .map(productRepository::save)
                .map(productReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<ProductReadDto> update(Long id, ProductCreateEditDto productDto) {
        return productRepository.findById(id)
                .map(product -> productCreateEditMapper.map(productDto, product))
                .map(productRepository::saveAndFlush)
                .map(productReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    productRepository.flush();
                    return true;
                })
                .orElse(false);
    }

}
