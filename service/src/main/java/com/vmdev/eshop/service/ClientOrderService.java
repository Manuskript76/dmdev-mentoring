package com.vmdev.eshop.service;

import com.vmdev.eshop.dto.ClientOrderReadDto;
import com.vmdev.eshop.mapper.ClientOrderReadMapper;
import com.vmdev.eshop.repository.ClientOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientOrderService {

    private final ClientOrderRepository clientOrderRepository;
    private final ClientOrderReadMapper clientOrderReadMapper;


    public List<ClientOrderReadDto> findAllByClient(Long clientId) {
        return clientOrderRepository.findAllByClientId(clientId).stream()
                .map(clientOrderReadMapper::map)
                .toList();
    }
}
