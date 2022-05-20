package com.upachar.web.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upachar.web.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUserId(Long userId);

	List<Order> findByIdIn(List<Long> orderIds);

}
