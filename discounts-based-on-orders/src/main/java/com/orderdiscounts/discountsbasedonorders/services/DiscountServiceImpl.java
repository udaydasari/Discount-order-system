package com.orderdiscounts.discountsbasedonorders.services;

import com.orderdiscounts.discountsbasedonorders.model.*;
import com.orderdiscounts.discountsbasedonorders.repository.CustomerRepository;
import com.orderdiscounts.discountsbasedonorders.repository.OrderItemRepository;
import com.orderdiscounts.discountsbasedonorders.repository.OrderRepository;
import com.orderdiscounts.discountsbasedonorders.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
//import lombok.var;
//import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Log4j2
public class DiscountServiceImpl implements DiscountService{
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Override
    public Double calculateDiscount(Order order) throws ParseException {

        double discountPercentage = 0.0;

        Customer customer = customerRepository.findById(order.getCustomerId())
                .orElseThrow(()->new RuntimeException("Customer Not found")
        );
        List<Order> orders = customer.getOrdersList();

        List<Order> orderIds= orderRepository.findAllOrderIdByCustomerId(order.getCustomerId());

        int orderCount=orderIds.size();
        System.out.println(orderCount);
        if(orderCount > 10 && orderCount <20)
            discountPercentage= 0.1;
        else if(orderCount > 20)
            discountPercentage= 0.2;



        Date orderDateString = order.getOrderDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH); // Date format of input string
        Date orderDate = null;
        orderDate = dateFormat.parse(String.valueOf(orderDateString));
        SimpleDateFormat bfcmDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date bfcmStartDate = bfcmDateFormat.parse("2022-11-25"); // BFCM start date
        Date bfcmEndDate = bfcmDateFormat.parse("2022-11-28"); // BFCM end date
        if (orderDate.after(bfcmStartDate) && orderDate.before(bfcmEndDate)
                || orderDate.equals(bfcmStartDate) || orderDate.equals(bfcmEndDate)) {
             discountPercentage= discountPercentage + 0.15;
             return discountPercentage;
        }


        return  discountPercentage;

    }

    @Override
    public Double calculateTotalAmount(List<OrderItems> productDetails) {

        List<Product> productList = productRepository.findAllById(productDetails.stream().map(x -> x.getProductId()).collect(Collectors.toList()));
         double a = 0.0;
         for(OrderItems x:productDetails){
             Product product= productRepository.findById(x.getProductId()).get();
             a = a+product.getPrice() * x.getQuantity();
         }

        return a;
    }

    @Override
    public OrderResponse saveCustomerAndOrder(Order order, Double discountPercentage, Double totalAmountBeforeDiscount) {
        long customerId= order.getCustomerId();

        Optional<Customer> customerOptional= customerRepository.findById(customerId);
        if (customerOptional.isPresent()) {
            var discountAmount = totalAmountBeforeDiscount * discountPercentage;
            var payableAmount = totalAmountBeforeDiscount - discountAmount;
            Order orderToBeSaved= order.builder()
                    .orderDate(order.getOrderDate())
                    .customerId(order.getCustomerId())
                    .productDetails(new ArrayList<>())
                    .totalAmountAfterDiscount((long)(payableAmount))
                    .actualAmountBeforeDiscount(totalAmountBeforeDiscount)
                    .discountGiven(discountAmount)
                    .build();

            Order savedOrder=orderRepository.save(orderToBeSaved);


            List<OrderItems> orderItems = new ArrayList<>();
            for(OrderItems productDetails: order.getProductDetails()) {
                OrderItems orderItems1 = OrderItems.builder()
                        .productId(productDetails.getProductId())
                        .quantity(productDetails.getQuantity())
                        .order(savedOrder)
                        .build();
                orderItems.add(orderItems1);

            }

            orderItemRepository.saveAll(orderItems);
            order.getProductDetails().addAll(orderItems);
       Order savedOrder1=orderRepository.save(orderToBeSaved);




        return OrderResponse.builder()
                    .orderId(savedOrder.getOrderId())
                    .customerId(savedOrder.getCustomerId())
                    .productDetails(orderItems)
                    .orderDate(savedOrder.getOrderDate())
                    .actualAmountBeforeDiscount(savedOrder.getActualAmountBeforeDiscount())
                    .totalAmountAfterDiscount(savedOrder.getTotalAmountAfterDiscount())
                    .discountGiven(savedOrder.getDiscountGiven())
                    .build();

        } else {
            throw new RuntimeException("Customer not found");

        }

    }
}
