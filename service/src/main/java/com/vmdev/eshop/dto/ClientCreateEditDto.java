package com.vmdev.eshop.dto;

import com.vmdev.eshop.entity.enums.Role;
import lombok.Value;

@Value
public class ClientCreateEditDto {
    String firstname;
    String lastname;
    String email;
    String phone;
    String address;
    Role role;
}
