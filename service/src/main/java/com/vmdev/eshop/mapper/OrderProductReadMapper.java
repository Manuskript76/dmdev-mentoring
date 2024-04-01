package com.vmdev.eshop.mapper;

import com.vmdev.eshop.dto.OrderProductReadDto;
import com.vmdev.eshop.entity.OrderProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProductReadMapper implements Mapper<OrderProduct, OrderProductReadDto> {

    private final ProductReadMapper productReadMapper;

    @Override
    public OrderProductReadDto map(OrderProduct object) {
        return new OrderProductReadDto(
                object.getId(),
                productReadMapper.map(object.getProduct()),
                object.getQuantity()
        );
    }
}
