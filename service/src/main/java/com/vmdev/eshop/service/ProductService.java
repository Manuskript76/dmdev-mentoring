package com.vmdev.eshop.service;

import com.querydsl.core.types.Predicate;
import com.vmdev.eshop.dto.ProductCreateEditDto;
import com.vmdev.eshop.dto.ProductFilter;
import com.vmdev.eshop.dto.ProductReadDto;
import com.vmdev.eshop.entity.Product;
import com.vmdev.eshop.filter.QPredicate;
import com.vmdev.eshop.mapper.ProductCreateEditMapper;
import com.vmdev.eshop.mapper.ProductReadMapper;
import com.vmdev.eshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.vmdev.eshop.entity.QProduct.product;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductReadMapper productReadMapper;
    private final ProductCreateEditMapper productCreateEditMapper;
    private final ImageService imageService;

    public List<ProductReadDto> findAll() {
        return productRepository.findAll().stream()
                .map(productReadMapper::map)
                .toList();
    }

    public Page<ProductReadDto> findAll(ProductFilter filter, Pageable pageable) {
        Predicate predicate = QPredicate.builder()
                .add(filter.getName(), product.name::containsIgnoreCase)
                .add(filter.getType(), product.type::eq)
                .add(filter.getManufacturer(), product.manufacturer::containsIgnoreCase)
                .add(filter.getCost(), product.cost::loe)
                .build();

        return productRepository.findAll(predicate, pageable)
                .map(productReadMapper::map);
    }

    public Optional<ProductReadDto> findById(Long id) {
        return productRepository.findById(id)
                .map(productReadMapper::map);
    }

    public Optional<byte[]> findAvatar(Long id) {
        return productRepository.findById(id)
                .map(Product::getImage)
                .filter(StringUtils::hasText)
                .flatMap(imageService::get);
    }

    @Transactional
    public ProductReadDto create(ProductCreateEditDto productDto) {
        return Optional.of(productDto)
                .map(dto -> {
                    uploadImage(dto.getImage());
                    return productCreateEditMapper.map(dto);
                })
                .map(productRepository::save)
                .map(productReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<ProductReadDto> update(Long id, ProductCreateEditDto productDto) {
        return productRepository.findById(id)
                .map(product -> {
                    if (productDto.getImage() != null) {
                        uploadImage(productDto.getImage());
                    }
                    return productCreateEditMapper.map(productDto, product);
                })
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

    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if (!image.isEmpty()) {
            imageService.upload(image.getOriginalFilename(), image.getInputStream());
        }
    }

}
