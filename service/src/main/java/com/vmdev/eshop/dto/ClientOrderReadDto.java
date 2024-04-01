package com.vmdev.eshop.dto;

import com.vmdev.eshop.entity.enums.OrderStatus;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
public class ClientOrderReadDto {
    Long id;
    LocalDate openDate;
    LocalDate closeDate;
    OrderStatus status;
    Integer productCount;
    Integer summaryCost;
    List<OrderProductReadDto> products;
}
