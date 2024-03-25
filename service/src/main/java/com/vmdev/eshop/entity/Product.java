package com.vmdev.eshop.entity;

import com.vmdev.eshop.entity.enums.ProductType;
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
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "name")
@ToString(exclude = {"orderProducts", "reviews"})
@Builder
@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Product extends AuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Integer cost;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    private String manufacturer;

    @Builder.Default
    @OneToMany(mappedBy = "product")
    @NotAudited
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product")
    @NotAudited
    private List<Review> reviews = new ArrayList<>();
}