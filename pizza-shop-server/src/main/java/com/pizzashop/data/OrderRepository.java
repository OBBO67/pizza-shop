package com.pizzashop.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pizzashop.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
