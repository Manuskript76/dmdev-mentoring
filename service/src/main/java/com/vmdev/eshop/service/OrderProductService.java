package com.vmdev.eshop.service;

import com.vmdev.eshop.dto.OrderProductDto;
import com.vmdev.eshop.entity.OrderProduct;
import com.vmdev.eshop.entity.Product;
import com.vmdev.eshop.mapper.OrderProductReadMapper;
import com.vmdev.eshop.repository.ClientOrderRepository;
import com.vmdev.eshop.repository.OrderProductRepository;
import com.vmdev.eshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;
    private final OrderProductReadMapper orderProductReadMapper;
    private final ProductRepository productRepository;
    private final ClientOrderRepository clientOrderRepository;

    @Transactional
    public OrderProductDto findOrCreateByClientAndOrder(Long orderId, Long productId) {
        Optional<OrderProduct> maybeOrderProduct = orderProductRepository.findByClientOrderIdAndProductId(orderId, productId);
        OrderProduct orderProduct = maybeOrderProduct.orElseThrow();
        return maybeOrderProduct.map(product -> create(orderId, productId))
                .orElseGet(() -> {
                    orderProduct.setQuantity(orderProduct.getQuantity() + 1);
                    orderProductRepository.saveAndFlush(orderProduct);
                    return orderProductReadMapper.map(orderProduct);
                });

    }

    @Transactional
    public OrderProductDto findAndRemoveByClientAndOrder(Long orderId, Long productId) {
        Optional<OrderProduct> maybeOrderProduct = orderProductRepository.findByClientOrderIdAndProductId(orderId, productId);
        OrderProduct orderProduct = maybeOrderProduct.orElseThrow();
        orderProduct.setQuantity(orderProduct.getQuantity() - 1);
        orderProductRepository.saveAndFlush(orderProduct);
        return orderProductReadMapper.map(orderProduct);
    }

    @Transactional
    public OrderProductDto create(Long orderId, Long productId) {
        OrderProduct orderProduct = new OrderProduct();
        Product product = productRepository.findById(productId).orElseThrow();
        orderProduct.setProduct(product);
        orderProduct.setClientOrder(clientOrderRepository.findById(orderId).orElseThrow());
        orderProduct.setQuantity(1);
        orderProductRepository.saveAndFlush(orderProduct);

        return orderProductReadMapper.map(orderProduct);
    }
}
