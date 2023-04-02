package com.orderdiscounts.discountsbasedonorders.repository;

import com.orderdiscounts.discountsbasedonorders.model.Order;
import com.orderdiscounts.discountsbasedonorders.model.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItems,Long> {
}
