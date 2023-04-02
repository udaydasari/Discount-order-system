package com.orderdiscounts.discountsbasedonorders.repository;

import com.orderdiscounts.discountsbasedonorders.model.Customer;
import com.orderdiscounts.discountsbasedonorders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
//@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Order> findOrderListById(long customerId);
}
