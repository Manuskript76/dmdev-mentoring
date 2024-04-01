package com.vmdev.eshop.dto;

import com.vmdev.eshop.entity.enums.Role;
import lombok.Value;

import java.util.List;

@Value
public class ClientReadDto {
    Long id;
    String firstname;
    String lastname;
    String email;
    String phone;
    String address;
    Role role;
    List<ClientOrderReadDto> orders;
}
