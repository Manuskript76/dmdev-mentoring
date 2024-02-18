package com.vmdev.eshop.entity;

import com.vmdev.eshop.entity.enums.ReviewGrade;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String review;

    @Enumerated(EnumType.STRING)
    private ReviewGrade grade;

    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public void setClient(Client client) {
        this.client = client;
        this.client.getReviews().add(this);
    }

    public void setProduct(Product product) {
        this.product = product;
        this.product.getReviews().add(this);
    }
}
