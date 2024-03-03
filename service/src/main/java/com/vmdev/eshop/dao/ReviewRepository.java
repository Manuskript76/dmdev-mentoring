package com.vmdev.eshop.dao;

import com.vmdev.eshop.entity.Review;
import jakarta.persistence.EntityManager;

public class ReviewRepository extends RepositoryBase<Long, Review> {
    public ReviewRepository(EntityManager entityManager) {
        super(Review.class, entityManager);
    }
}
