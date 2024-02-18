package com.vmdev.eshop.entity;

import com.vmdev.eshop.entity.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "products")
@Entity
public class ClientOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate openDate;

    private LocalDate closeDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Integer productCount;

    private Integer summaryCost;

    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    @Builder.Default
    @OneToMany(mappedBy = "clientOrder")
    private List<OrderProduct> products = new ArrayList<>();

    public void addProduct(OrderProduct orderProduct) {
        products.add(orderProduct);
        orderProduct.setClientOrder(this);
    }
}
