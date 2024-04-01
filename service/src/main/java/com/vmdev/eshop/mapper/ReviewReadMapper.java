package com.vmdev.eshop.mapper;

import com.vmdev.eshop.dto.ReviewReadDto;
import com.vmdev.eshop.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewReadMapper implements Mapper<Review, ReviewReadDto> {

    @Override
    public ReviewReadDto map(Review object) {
        return new ReviewReadDto(
                object.getDate(),
                object.getReview(),
                object.getGrade()
        );
    }
}
