package com.orderdiscounts.discountsbasedonorders.services;

import com.orderdiscounts.discountsbasedonorders.model.Order;
import com.orderdiscounts.discountsbasedonorders.model.OrderItems;

import java.text.ParseException;
import java.util.List;

public interface DiscountService{
    Double calculateDiscount(Order order) throws ParseException;

    Double calculateTotalAmount(List<OrderItems> productDetails);

    Order saveCustomerAndOrder(Order order, Double discountPercentage, Double totalAmountBeforeDiscount);
}
