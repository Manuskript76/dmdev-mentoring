package com.vmdev.eshop.http.controller;

import com.vmdev.eshop.dto.ClientOrderDto;
import com.vmdev.eshop.dto.OrderProductDto;
import com.vmdev.eshop.dto.ProductCreateEditDto;
import com.vmdev.eshop.dto.ProductReadDto;
import com.vmdev.eshop.entity.enums.OrderStatus;
import com.vmdev.eshop.service.ClientOrderService;
import com.vmdev.eshop.service.ClientService;
import com.vmdev.eshop.service.OrderProductService;
import com.vmdev.eshop.service.ProductService;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
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

import java.time.LocalDate;
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
    private final ClientService clientService;

    @PostMapping("/{id}/add")
    public String addProductToOrder(@PathVariable("id") Long id,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        ClientOrderDto clientOrder = clientOrderService.findByClientUsername(userDetails.getUsername()).orElseThrow();

        if (clientOrder.getStatus().equals(OrderStatus.COMPLETED)) {
            clientOrderService.delete(clientOrder.getId());
            clientOrder = clientOrderService.create(clientService.findByEmail(userDetails.getUsername()).get().getId());
        }
        ProductReadDto product = productService.findById(id).orElseThrow();

        if (product.getQuantity() == 0) {
            return "error/error500";
        }
        OrderProductDto orderProduct = orderProductService.incrementOrCreateByClientAndOrder(clientOrder.getId(), product.getId());

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
        OrderProductDto orderProduct = orderProductService.decrementByClientAndOrder(clientOrder.getId(), product.getId());
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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @PostMapping("/{id}/checkout")
    public String checkout(@PathVariable("id") Long id) {
        ClientOrderDto clientOrder = clientOrderService.findById(id).orElseThrow();
        List<OrderProductDto> products = new ArrayList<>();
        for (int i = 0; i <= clientOrder.getProducts().size() - 1; i++) {
            for (int j = 0; j < clientOrder.getProducts().get(i).getQuantity(); j++) {
                products.add(clientOrder.getProducts().get(i));
            }
        }

        for (int i = 0; i < clientOrder.getProducts().size(); i++) {
            if (clientOrder.getProducts().get(i).getQuantity() > productService.findById(clientOrder.getProducts().get(i).getProduct().getId()).orElseThrow().getQuantity()) {
                return "error/error500";
            }
        }

        products.stream()
                .map(OrderProductDto::getProduct)
                .map(orderProductDto -> productService.findById(orderProductDto.getId()))
                .map(Optional::orElseThrow)
                .map(productReadDto -> ProductCreateEditDto.builder()
                        .id(productReadDto.getId())
                        .name(productReadDto.getName())
                        .description(productReadDto.getDescription())
                        .cost(productReadDto.getCost())
                        .quantity(productService.findById(productReadDto.getId()).orElseThrow().getQuantity() - 1)
                        .type(productReadDto.getType())
                        .manufacturer(productReadDto.getManufacturer())
                        .build())
                .forEach(productCreateEditDto -> productService.update(productCreateEditDto.getId(), productCreateEditDto));

        ClientOrderDto clientOrderDto = ClientOrderDto.builder()
                .id(clientOrder.getId())
                .openDate(clientOrder.getOpenDate())
                .closeDate(LocalDate.now())
                .status(OrderStatus.COMPLETED)
                .productCount(clientOrder.getProductCount())
                .summaryCost(clientOrder.getSummaryCost())
                .products(clientOrder.getProducts())
                .build();
        clientOrderService.update(clientOrderDto);

        return "redirect:/orders/" + id;
    }

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
