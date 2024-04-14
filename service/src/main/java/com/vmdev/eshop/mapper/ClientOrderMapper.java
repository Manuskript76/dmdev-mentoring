package com.vmdev.eshop.mapper;

import com.vmdev.eshop.dto.ClientOrderDto;
import com.vmdev.eshop.entity.ClientOrder;
import com.vmdev.eshop.entity.OrderProduct;
import com.vmdev.eshop.repository.ClientOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ClientOrderMapper implements Mapper<ClientOrder, ClientOrderDto> {

    private final OrderProductReadMapper orderProductReadMapper;
    private final OrderProductReadMapper productReadMapper;
    private final ClientOrderRepository clientOrderRepository;

    public ClientOrder map(ClientOrderDto fromObject, ClientOrder toObject) {
        toObject.setId(fromObject.getId());
        toObject.setProducts(fromObject.getProducts().stream().map(productReadMapper::map).toList());
        toObject.setStatus(fromObject.getStatus());
        toObject.setOpenDate(fromObject.getOpenDate());
        toObject.setCloseDate(fromObject.getCloseDate());
        toObject.setSummaryCost(fromObject.getSummaryCost());
        toObject.setProductCount(fromObject.getProductCount());
        return toObject;
    }

    @Override
    public ClientOrderDto map(ClientOrder object) {

        return new ClientOrderDto(
                object.getId(),
                object.getOpenDate(),
                object.getCloseDate(),
                object.getStatus(),
                object.getProductCount(),
                object.getSummaryCost(),
                object.getProducts().stream().map(orderProductReadMapper::map).toList()
        );
    }

    public ClientOrder map(ClientOrderDto object) {
        ClientOrder clientOrder = clientOrderRepository.findById(object.getId()).orElseThrow();
        clientOrder.setId(object.getId());
        clientOrder.setOpenDate(object.getOpenDate());
        clientOrder.setCloseDate(object.getCloseDate());
        clientOrder.setStatus(object.getStatus());
        clientOrder.setProductCount(object.getProductCount());
        clientOrder.setSummaryCost(object.getSummaryCost());
        List<OrderProduct> products = new ArrayList<>(object.getProducts().stream().map(orderProductReadMapper::map).toList());
        clientOrder.setProducts(products);
        return clientOrder;
    }
}
