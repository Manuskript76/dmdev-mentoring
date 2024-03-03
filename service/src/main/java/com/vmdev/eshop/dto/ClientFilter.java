package com.vmdev.eshop.dto;

import com.vmdev.eshop.entity.enums.Role;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ClientFilter {
    String firstName;

    String lastName;

    String email;

    String phone;

    String address;

    Role role;
}