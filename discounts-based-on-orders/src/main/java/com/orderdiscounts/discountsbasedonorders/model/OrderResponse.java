package com.orderdiscounts.discountsbasedonorders.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.criterion.Order;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    private Long orderId;
    private Long customerId;
    private List<OrderItems> productDetails;
    private Date orderDate;
    private Double actualAmountBeforeDiscount;
    private Double totalAmountAfterDiscount;
    private Double discountGiven;
}
