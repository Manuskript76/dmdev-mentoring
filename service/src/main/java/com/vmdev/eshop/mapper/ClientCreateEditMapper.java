package com.vmdev.eshop.mapper;

import com.vmdev.eshop.dto.ClientCreateEditDto;
import com.vmdev.eshop.entity.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientCreateEditMapper implements Mapper<ClientCreateEditDto, Client> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public Client map(ClientCreateEditDto fromObject, Client toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public Client map(ClientCreateEditDto object) {
        Client client = new Client();
        copy(object, client);
        return client;
    }

    private void copy(ClientCreateEditDto object, Client client) {
        client.setFirstname(object.getFirstname());
        client.setLastname(object.getLastname());
        client.setEmail(object.getEmail());
        client.setPhone(object.getPhone());
        client.setAddress(object.getAddress());
        client.setRole(object.getRole());

        Optional.ofNullable(object.getRawPassword())
                .filter(StringUtils::hasText)
                .map(passwordEncoder::encode)
                .ifPresent(client::setPassword);
    }
}
