package com.vmdev.eshop.mapper;

import com.vmdev.eshop.dto.ClientOrderReadDto;
import com.vmdev.eshop.entity.ClientOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientOrderReadMapper implements Mapper<ClientOrder, ClientOrderReadDto> {

    private final OrderProductReadMapper orderProductReadMapper;

    @Override
    public ClientOrderReadDto map(ClientOrder object) {
        return new ClientOrderReadDto(
                object.getId(),
                object.getOpenDate(),
                object.getCloseDate(),
                object.getStatus(),
                object.getProductCount(),
                object.getSummaryCost(),
                object.getProducts().stream().map(orderProductReadMapper::map).toList()
        );
    }
}
