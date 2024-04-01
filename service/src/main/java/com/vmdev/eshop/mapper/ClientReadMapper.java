package com.vmdev.eshop.mapper;

import com.vmdev.eshop.dto.ClientReadDto;
import com.vmdev.eshop.entity.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientReadMapper implements Mapper<Client, ClientReadDto> {

    private final ClientOrderReadMapper clientOrderReadMapper;

    @Override
    public ClientReadDto map(Client object) {
        return new ClientReadDto(
                object.getId(),
                object.getFirstname(),
                object.getLastname(),
                object.getEmail(),
                object.getPhone(),
                object.getAddress(),
                object.getRole(),
                object.getOrders().stream().map(clientOrderReadMapper::map).toList()
        );
    }
}
