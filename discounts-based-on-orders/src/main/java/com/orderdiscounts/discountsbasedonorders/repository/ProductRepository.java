package com.orderdiscounts.discountsbasedonorders.repository;

import com.orderdiscounts.discountsbasedonorders.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
