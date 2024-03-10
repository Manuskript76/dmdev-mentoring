package com.vmdev.eshop.repository;

import com.vmdev.eshop.TestRepositoryBase;
import com.vmdev.eshop.entity.Review;
import com.vmdev.eshop.entity.enums.ReviewGrade;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReviewRepositoryIT extends TestRepositoryBase {

    private static ReviewRepository reviewRepository;
    private final Long REVIEW_ID = 1L;
    @BeforeAll
    static void init() {
        reviewRepository = context.getBean("reviewRepository", ReviewRepository.class);
    }

    @Test
    void findReviewById() {
        Review expectedResult = entityManager.find(Review.class, REVIEW_ID);
        Optional<Review> actualResult = reviewRepository.findById(REVIEW_ID);

        assertThat(actualResult).isPresent();
        assertEquals(expectedResult, actualResult.get());
    }

    @Test
    void findNothingIfReviewNotExist() {
        Optional<Review> actualResult = reviewRepository.findById(100000L);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void findAllReviewCheck() {
        List<Review> actualResult = reviewRepository.findAll();

        assertThat(actualResult).hasSize(4);
    }

    @Test
    void createReviewCheck() {
        Review review = Review.builder()
                .review("review")
                .date(LocalDate.now())
                .grade(ReviewGrade.GOOD)
                .build();

        reviewRepository.save(review);
        Optional<Review> actualResult = reviewRepository.findById(review.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(review.getId());
    }

    @Test
    void deleteReviewCheck() {
        Review review = Review.builder()
                .review("review")
                .date(LocalDate.now())
                .grade(ReviewGrade.GOOD)
                .build();

        reviewRepository.save(review);
        reviewRepository.delete(review);
        Optional<Review> actualResult = reviewRepository.findById(review.getId());

        assertThat(actualResult).isEmpty();
    }

    @Test
    void updateReviewCheck() {
        Review review = Review.builder()
                .review("review")
                .date(LocalDate.now())
                .grade(ReviewGrade.GOOD)
                .build();
        review.setGrade(ReviewGrade.EXCELLENT);

        reviewRepository.save(review);
        reviewRepository.update(review);
        entityManager.flush();
        Optional<Review> actualResult = reviewRepository.findById(review.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getGrade()).isSameAs(ReviewGrade.EXCELLENT);
    }

}