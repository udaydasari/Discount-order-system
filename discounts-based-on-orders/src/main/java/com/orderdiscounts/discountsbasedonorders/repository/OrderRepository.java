package com.orderdiscounts.discountsbasedonorders.repository;

import com.orderdiscounts.discountsbasedonorders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
