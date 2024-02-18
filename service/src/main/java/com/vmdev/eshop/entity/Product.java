package com.vmdev.eshop.entity;

import com.vmdev.eshop.entity.enums.ProductType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "name")
@ToString(exclude = {"orderProduct", "reviews"})
@Builder
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Integer cost;

    private Integer quantity;

    private String manufacturer;
    //todo: мб лучше сделать Enum

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Builder.Default
    @OneToMany(mappedBy = "product")
    private List<OrderProduct> orderProduct = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product")
    private List<Review> reviews = new ArrayList<>();
}
