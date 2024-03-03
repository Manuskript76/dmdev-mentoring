package com.vmdev.eshop.dao;

import com.vmdev.eshop.entity.Review;
import com.vmdev.eshop.entity.enums.ReviewGrade;
import com.vmdev.eshop.util.HibernateTestUtil;
import com.vmdev.eshop.util.SessionProxy;
import com.vmdev.eshop.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReviewRepositoryIT {

    private static SessionFactory sessionFactory;
    private Session session;
    private ReviewRepository reviewRepository;

    @BeforeAll
    static void buildSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        TestDataImporter.importData(sessionFactory);
    }

    @BeforeEach
    void init() {
        session = SessionProxy.getSession(sessionFactory);
        reviewRepository = new ReviewRepository(session);
        session.beginTransaction();
    }

    @AfterEach
    void close() {
        session.getTransaction().rollback();
        session.close();
    }

    @AfterAll
    static void closeSessionFactory() {
        sessionFactory.close();
    }

    @Test
    void findReviewById() {
        Review expectedResult = session.get(Review.class, 1L);
        Optional<Review> actualResult = reviewRepository.findById(1L);

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
        session.flush();
        Optional<Review> actualResult = reviewRepository.findById(review.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getGrade()).isSameAs(ReviewGrade.EXCELLENT);
    }

}