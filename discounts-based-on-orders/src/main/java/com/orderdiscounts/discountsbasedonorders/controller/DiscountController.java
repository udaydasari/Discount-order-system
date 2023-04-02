package com.orderdiscounts.discountsbasedonorders.controller;

import com.orderdiscounts.discountsbasedonorders.model.Order;
import com.orderdiscounts.discountsbasedonorders.model.OrderResponse;
import com.orderdiscounts.discountsbasedonorders.repository.CustomerRepository;
import com.orderdiscounts.discountsbasedonorders.repository.OrderRepository;
import com.orderdiscounts.discountsbasedonorders.services.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/orders")
public class DiscountController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private DiscountService discountService;

    @PostMapping("/placeOrder")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody Order order) throws ParseException {
        Double discountPercentage = discountService.calculateDiscount(order);
        Double totalAmountBeforeDiscount= discountService.calculateTotalAmount(order.getProductDetails());
        System.out.println(discountPercentage);
        System.out.println(totalAmountBeforeDiscount);
        System.out.println();
        OrderResponse orderId=discountService.saveCustomerAndOrder(order,discountPercentage,totalAmountBeforeDiscount);


        return new ResponseEntity<>(orderId,HttpStatus.OK);
    }
}
