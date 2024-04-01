package com.vmdev.eshop.mapper;

import com.vmdev.eshop.dto.ClientCreateEditDto;
import com.vmdev.eshop.entity.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientCreateEditMapper implements Mapper<ClientCreateEditDto, Client> {

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

    private static void copy(ClientCreateEditDto object, Client client) {
        client.setFirstname(object.getFirstname());
        client.setLastname(object.getLastname());
        client.setEmail(object.getEmail());
        client.setPhone(object.getPhone());
        client.setAddress(object.getAddress());
        client.setRole(object.getRole());
    }
}
