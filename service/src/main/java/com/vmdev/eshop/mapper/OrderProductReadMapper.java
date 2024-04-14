package com.vmdev.eshop.mapper;

import com.vmdev.eshop.dto.OrderProductDto;
import com.vmdev.eshop.entity.OrderProduct;
import com.vmdev.eshop.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProductReadMapper implements Mapper<OrderProductDto, OrderProduct> {

    private final ProductReadMapper productReadMapper;
    private final OrderProductRepository orderProductRepository;

    public OrderProductDto map(OrderProduct object) {
        return new OrderProductDto(
                object.getId(),
                productReadMapper.map(object.getProduct()),
                object.getQuantity()
        );
    }

    @Override
    public OrderProduct map(OrderProductDto fromObject, OrderProduct toObject) {
        toObject.setId(fromObject.getId());
        toObject.setQuantity(fromObject.getQuantity());
        toObject.setProduct(productReadMapper.map(fromObject.getProduct()));
        return toObject;
    }

    @Override
    public OrderProduct map(OrderProductDto productReadDto) {
        OrderProduct orderProduct = orderProductRepository.findById(productReadDto.getId()).orElseThrow();
        orderProduct.setProduct(productReadMapper.map(productReadDto.getProduct()));
        orderProduct.setQuantity(productReadDto.getQuantity());
        orderProduct.setQuantity(productReadDto.getQuantity());
        // TODO: 14.04.2024 подправить логику
        orderProduct.setId(orderProduct.getId());
        return orderProduct;
    }

}
