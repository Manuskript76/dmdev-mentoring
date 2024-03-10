package com.vmdev.eshop.repository;

import com.vmdev.eshop.entity.Review;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepository extends RepositoryBase<Long, Review> {
    public ReviewRepository(EntityManager entityManager) {
        super(Review.class, entityManager);
    }
}
