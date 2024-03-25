package com.vmdev.eshop.repository;

import com.querydsl.core.types.Predicate;
import com.vmdev.eshop.dto.ProductFilter;
import com.vmdev.eshop.entity.Product;
import com.vmdev.eshop.entity.QProduct;
import com.vmdev.eshop.entity.enums.ProductType;
import com.vmdev.eshop.filter.QPredicate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class ProductRepositoryIT extends IntegrationTestBase {

    private final ProductRepository productRepository;

    @Test
    void findAllProductsWithNameFilter() {
        ProductFilter filter = getFilter("MBR-pr15D", null, null, null);
        Predicate predicate = getPredicate(filter);
        Iterable<Product> actualResult = productRepository.findAll(predicate);

        actualResult.forEach(product -> assertThat(product.getName()).isEqualTo(filter.getName()));
    }

    @Test
    void findAllProductsWithManufacturerFilter() {
        ProductFilter filter = getFilter(null, null, "PHILIPS", null);
        Predicate predicate = getPredicate(filter);
        Iterable<Product> actualResult = productRepository.findAll(predicate);

        actualResult.forEach(product -> assertThat(product.getManufacturer()).isEqualTo(filter.getManufacturer()));
    }

    @Test
    void findAllProductsWithCostFilter() {
        ProductFilter filter = getFilter(null, null, null, 70000);
        Predicate predicate = getPredicate(filter);
        Iterable<Product> actualResult = productRepository.findAll(predicate);

        actualResult.forEach(product -> assertThat(product.getCost()).isLessThan(filter.getCost()));
    }

    @Test
    void findAllProductsWithTypeFilter() {
        ProductFilter filter = getFilter(null, ProductType.TV, null, null);
        Predicate predicate = getPredicate(filter);
        Iterable<Product> actualResult = productRepository.findAll(predicate);

        actualResult.forEach(product -> assertThat(product.getType()).isSameAs(filter.getType()));
    }

    @Test
    void findNoClientsWithWrongFilter() {
        ProductFilter filter = getFilter(null, null, "null", null);
        Predicate predicate = getPredicate(filter);
        Iterable<Product> actualResult = productRepository.findAll(predicate);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void findClientsWithSeveralFilters() {
        ProductFilter filter = getFilter("MBR-pr15D", ProductType.OFFICE, "PHILIPS", 10000);
        Predicate predicate = getPredicate(filter);
        Iterable<Product> actualResult = productRepository.findAll(predicate);

        assertThat(actualResult).hasSize(1);
    }

    public Predicate getPredicate(ProductFilter filter) {
        return QPredicate.builder()
                .add(filter.getName(), QProduct.product.name::eq)
                .add(filter.getType(), QProduct.product.type::eq)
                .add(filter.getManufacturer(), QProduct.product.manufacturer::eq)
                .add(filter.getCost(), QProduct.product.cost::loe)
                .build();
    }

    public ProductFilter getFilter(String name, ProductType type, String manufacturer, Integer cost) {
        return ProductFilter.builder()
                .name(name)
                .type(type)
                .manufacturer(manufacturer)
                .cost(cost)
                .build();
    }
}