package com.orderdiscounts.discountsbasedonorders.services;

import com.orderdiscounts.discountsbasedonorders.model.Customer;
import com.orderdiscounts.discountsbasedonorders.model.Order;
import com.orderdiscounts.discountsbasedonorders.model.OrderItems;
import com.orderdiscounts.discountsbasedonorders.model.Product;
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

//        var
        Customer customer = customerRepository.findById(order.getCustomerId()).get();
        var orders= customer.getOrdersList();
//         List<Order> ordersList= customerRepository.findOrderListById(order.getCustomerId());
        int orderCount=orders.size();

        if(orderCount > 10 && orderCount <20)
            discountPercentage= 0.1;
        else if(orderCount > 20)
            discountPercentage= 0.2;



        Date orderDateString = order.getOrderDate(); // Example order date

        log.info(order.getOrderDate());
        System.out.println(order.getOrderDate());
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
    public Order saveCustomerAndOrder(Order order, Double discountPercentage, Double totalAmountBeforeDiscount) {
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
            for(OrderItems productDetails: order.getProductDetails()) {
                orderToBeSaved.getProductDetails().add(productDetails);
//                Product product = productRepository.findById(productDetails.getProductId())
//                        .orElseThrow(() -> new RuntimeException("Product not found"));
//                orderToBeSaved.setActualAmountBeforeDiscount(order.getActualAmountBeforeDiscount() + product.getPrice() * productDetails.getQuantity());
            }
             Order savedorder=orderRepository.save(orderToBeSaved);

        List<OrderItems> orderItems = order.getProductDetails();

        for(OrderItems x:order.getProductDetails()){
            x.setOrder(orderToBeSaved);
            orderItemRepository.save(x);
        }

        return orderRepository.findById(orderToBeSaved.getOrderId()).get();

        } else {
            throw new RuntimeException("Customer not found");

        }

    }
}
