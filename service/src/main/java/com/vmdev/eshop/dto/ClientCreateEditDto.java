package com.vmdev.eshop.dto;

import com.vmdev.eshop.entity.enums.Role;
import com.vmdev.eshop.validation.group.CreateAction;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class ClientCreateEditDto {

    @NotBlank
    String firstname;

    @NotBlank
    String lastname;

    @NotBlank
    @Email
    String email;

    @NotBlank(groups = CreateAction.class)
    String rawPassword;

    @NotBlank
    String phone;

    @NotBlank
    String address;

    @NotNull
    Role role;
}
