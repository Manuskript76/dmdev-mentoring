package com.vmdev.eshop.dto;

import com.vmdev.eshop.entity.enums.ReviewGrade;
import lombok.Value;

import java.time.LocalDate;

@Value
public class ReviewReadDto {
    LocalDate date;
    String review;
    ReviewGrade grade;
}
