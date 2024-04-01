package com.vmdev.eshop.service;

import com.vmdev.eshop.dto.ProductCreateEditDto;
import com.vmdev.eshop.dto.ProductReadDto;
import com.vmdev.eshop.entity.enums.ProductType;
import com.vmdev.eshop.repository.IntegrationTestBase;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class ProductServiceIT extends IntegrationTestBase {

    private final ProductService productService;
    private final EntityManager entityManager;

    @Test
    void findAll() {
        List<ProductReadDto> actualResult = productService.findAll();

        assertThat(actualResult).hasSize(10);
    }

    @Test
    void findById() {
        ProductReadDto product = productService.create(getProduct("PrinterTest"));
        entityManager.clear();
        Optional<ProductReadDto> actualResult = productService.findById(product.getId());

        assertThat(actualResult).isPresent();
        actualResult.ifPresent(productReadDto -> assertThat(productReadDto.getName()).isEqualTo(product.getName()));
    }

    @Test
    void create() {
        ProductCreateEditDto product = getProduct("PrinterTest");

        ProductReadDto actualResult = productService.create(product);

        assertThat(actualResult.getName()).isEqualTo(product.getName());
    }

    @Test
    void update() {
        ProductReadDto product = productService.create(getProduct("PrinterTest"));
        entityManager.clear();
        Optional<ProductReadDto> actualResult = productService.update(product.getId(), getProduct("PrinterTestUpdated"));

        assertThat(actualResult).isPresent();
        actualResult.ifPresent(productReadDto -> assertThat(productReadDto.getName()).isEqualTo("PrinterTestUpdated"));
    }

    @Test
    void delete() {
        ProductReadDto product = productService.create(getProduct("PrinterTest"));
        entityManager.clear();
        assertTrue(productService.delete(product.getId()));
        assertFalse(productService.delete(-121L));
    }

    private ProductCreateEditDto getProduct(String name) {
        return new ProductCreateEditDto(
                name,
                "printer",
                12500,
                4,
                ProductType.OFFICE,
                "HP"
        );
    }
}