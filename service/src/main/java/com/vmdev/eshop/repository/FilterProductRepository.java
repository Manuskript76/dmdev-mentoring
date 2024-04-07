package com.vmdev.eshop.repository;

import com.vmdev.eshop.dto.ProductFilter;
import com.vmdev.eshop.entity.Product;

import java.util.List;

public interface FilterProductRepository {

    List<Product> findAllByFilter(ProductFilter filter);
}
