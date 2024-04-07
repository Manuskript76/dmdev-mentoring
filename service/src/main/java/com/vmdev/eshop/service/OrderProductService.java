package com.vmdev.eshop.service;

import com.vmdev.eshop.dto.OrderProductReadDto;
import com.vmdev.eshop.entity.ClientOrder;
import com.vmdev.eshop.mapper.OrderProductReadMapper;
import com.vmdev.eshop.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;
    private final OrderProductReadMapper orderProductReadMapper;

    public List<OrderProductReadDto> findAllByOrder(ClientOrder clientOrder) {
        return orderProductRepository.findAllByClientOrder(clientOrder).stream()
                .map(orderProductReadMapper::map)
                .toList();
    }
}
