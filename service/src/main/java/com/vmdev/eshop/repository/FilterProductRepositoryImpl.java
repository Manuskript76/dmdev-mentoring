package com.vmdev.eshop.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.vmdev.eshop.dto.ProductFilter;
import com.vmdev.eshop.entity.Product;
import com.vmdev.eshop.filter.QPredicate;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.vmdev.eshop.entity.QProduct.product;

@RequiredArgsConstructor
public class FilterProductRepositoryImpl implements FilterProductRepository {

    private final EntityManager entityManager;

    @Override
    public List<Product> findAllByFilter(ProductFilter filter) {
        Predicate predicate = QPredicate.builder()
                .add(filter.getName(), product.name::containsIgnoreCase)
                .add(filter.getType(), product.type::eq)
                .add(filter.getManufacturer(), product.manufacturer::containsIgnoreCase)
                .add(filter.getCost(), product.cost::loe)
                .build();

        return new JPAQuery<Product>(entityManager)
                .select(product)
                .from(product)
                .where(predicate)
                .fetch();
    }
}
