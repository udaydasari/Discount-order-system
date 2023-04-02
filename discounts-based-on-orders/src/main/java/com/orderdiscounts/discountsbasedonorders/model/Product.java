package com.orderdiscounts.discountsbasedonorders.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long productId;

    //@Column(nullable = false)
    private String productName;

    //@Column(nullable = false)
    private Double price;

    //@Column(nullable = false)
    private long inStockProduct;
}
