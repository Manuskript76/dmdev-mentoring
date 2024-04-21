package com.vmdev.eshop.http.controller;

import com.vmdev.eshop.dto.ClientOrderDto;
import com.vmdev.eshop.dto.ClientReadDto;
import com.vmdev.eshop.dto.PageResponse;
import com.vmdev.eshop.dto.ProductCreateEditDto;
import com.vmdev.eshop.dto.ProductFilter;
import com.vmdev.eshop.dto.ProductReadDto;
import com.vmdev.eshop.entity.enums.ProductType;
import com.vmdev.eshop.service.ClientOrderService;
import com.vmdev.eshop.service.ClientService;
import com.vmdev.eshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
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
    private final ClientOrderService clientOrderService;
    private final ClientService clientService;

    @GetMapping
    public String findAll(Model model,
                          ProductFilter filter,
                          Pageable pageable,
                          @AuthenticationPrincipal UserDetails userDetails) {
        Page<ProductReadDto> page = productService.findAll(filter, pageable);
        ClientReadDto client = clientService.findByEmail(userDetails.getUsername())
                .orElseThrow();
        clientOrderService.findByClientUsername(userDetails.getUsername());
        ClientOrderDto order = clientOrderService.findByClientUsername(userDetails.getUsername())
                .map(dto -> clientOrderService.create(client.getId()))
                .orElseThrow();

        model.addAttribute("products", PageResponse.of(page));
        model.addAttribute("types", ProductType.values());
        model.addAttribute("filter", filter);
        model.addAttribute("order", order);
        return "product/products";
    }

    @GetMapping("/new")
    public String createProduct(Model model) {
        model.addAttribute("types", ProductType.values());
        return "product/newProduct";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id,
                           Model model,
                           @AuthenticationPrincipal UserDetails userDetails) {
        return productService.findById(id)
                .map(product -> {
                    model.addAttribute("order", clientOrderService.findByClientUsername(userDetails.getUsername()));
                    model.addAttribute("product", product);
                    model.addAttribute("types", ProductType.values());
                    model.addAttribute("reviews", product.getReviews());
                    return "product/product";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public String create(@Validated ProductCreateEditDto productDto) {
        return "redirect:/products/" + productService.create(productDto).getId();
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, @Validated ProductCreateEditDto productDto) {
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
