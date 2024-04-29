package com.vmdev.eshop.http.controller;

import com.vmdev.eshop.dto.ClientOrderDto;
import com.vmdev.eshop.dto.OrderProductDto;
import com.vmdev.eshop.dto.ProductReadDto;
import com.vmdev.eshop.service.ClientOrderService;
import com.vmdev.eshop.service.OrderProductService;
import com.vmdev.eshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final ClientOrderService clientOrderService;
    private final ProductService productService;
    private final OrderProductService orderProductService;

    @PostMapping("/{id}/add")
    public String addProductToOrder(@PathVariable("id") Long id,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        ClientOrderDto clientOrder = clientOrderService.findByClientUsername(userDetails.getUsername()).orElseThrow();
        ProductReadDto product = productService.findById(id).orElseThrow();
        OrderProductDto orderProduct = orderProductService.findOrCreateByClientAndOrder(clientOrder.getId(), product.getId());

        List<OrderProductDto> products = new ArrayList<>(clientOrder.getProducts());
        Optional<OrderProductDto> match = products.stream()
                .filter(productDto -> productDto.getId().equals(orderProduct.getId()))
                .findAny();
        if (match.isEmpty()) {
            products.add(orderProduct);
        }

        ClientOrderDto orderDto = ClientOrderDto.builder()
                .id(clientOrder.getId())
                .openDate(clientOrder.getOpenDate())
                .status(clientOrder.getStatus())
                .productCount(clientOrder.getProductCount() + 1)
                .summaryCost(clientOrder.getSummaryCost() + product.getCost())
                .products(products)
                .build();
        clientOrderService.update(orderDto);
        return "redirect:/products/" + id;
    }

    @PostMapping("/{id}/remove")
    public String removeProductFromOrder(@PathVariable("id") Long id, Long productId) {
        ClientOrderDto clientOrder = clientOrderService.findById(id).orElseThrow();
        ProductReadDto product = productService.findById(productId).orElseThrow();
        OrderProductDto orderProduct = orderProductService.findAndRemoveByClientAndOrder(clientOrder.getId(), product.getId());
        clientOrder = clientOrderService.findById(id).orElseThrow();

        List<OrderProductDto> products = new ArrayList<>(clientOrder.getProducts());
        if (orderProduct.getQuantity() == 0) {
            products.remove(orderProduct);
        }

        ClientOrderDto orderDto = ClientOrderDto.builder()
                .id(clientOrder.getId())
                .openDate(clientOrder.getOpenDate())
                .status(clientOrder.getStatus())
                .productCount(clientOrder.getProductCount() - 1)
                .summaryCost(clientOrder.getSummaryCost() - product.getCost())
                .products(products)
                .build();
        clientOrderService.update(orderDto);
        return "redirect:/orders/" + id;
    }


//    @PostMapping("/{id}/checkout")
//    public String checkout(@PathVariable("id") Long id) {
//        ClientOrderDto clientOrder = clientOrderService.findById(id).orElseThrow();
//        clientOrder.getProducts().stream()
//                .map(orderProductDto ->  productService.findById(orderProductDto.getId()))
//                .map(Optional::orElseThrow)
//                .map(dto -> new ProductReadDto(dto.getId(), dto.getName(), dto.getDescription(), dto.getCost(), dto.getQuantity() ))
//    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return clientOrderService.findById(id)
                .map(order -> {
                    model.addAttribute("order", order);
                    return "orders/order";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
