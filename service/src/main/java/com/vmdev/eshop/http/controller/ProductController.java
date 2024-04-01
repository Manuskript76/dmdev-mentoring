package com.vmdev.eshop.http.controller;

import com.vmdev.eshop.dto.ProductCreateEditDto;
import com.vmdev.eshop.dto.ProductFilter;
import com.vmdev.eshop.entity.enums.ProductType;
import com.vmdev.eshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public String findAll(Model model, ProductFilter filter) {
        model.addAttribute("products", productService.findAll());
        return "product/products";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return productService.findById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    model.addAttribute("types", ProductType.values());
                    model.addAttribute("reviews", product.getReviews());
                    return "product/product";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public String create(ProductCreateEditDto productDto) {
        return "redirect:/products/" + productService.create(productDto).getId();
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, ProductCreateEditDto productDto) {
        return productService.update(id, productDto)
                .map(it -> "redirect:/products/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (productService.delete(id)) {
            return "redirect:/products";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
