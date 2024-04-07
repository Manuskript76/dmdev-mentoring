package com.vmdev.eshop.dto;

import com.vmdev.eshop.entity.enums.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

@Value
public class ProductCreateEditDto {

    @NotBlank
    String name;

    @Size(min = 10, max = 64)
    String description;

    @NotNull
    Integer cost;

    @NotNull
    Integer quantity;

    ProductType type;

    @NotBlank
    String manufacturer;

    MultipartFile image;
}
