package com.orderdiscounts.discountsbasedonorders.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")

public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long orderId;

    private long customerId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "productDetails")
    private List<OrderItems> productDetails = new ArrayList<>();

    private Date orderDate;  //= Date.from(Instant.now());

    private double actualAmountBeforeDiscount;

    private double totalAmountAfterDiscount;

    private double discountGiven;

    @ManyToOne
    @JoinColumn(name = "customer_customer_id")
    private Customer customer;


}

