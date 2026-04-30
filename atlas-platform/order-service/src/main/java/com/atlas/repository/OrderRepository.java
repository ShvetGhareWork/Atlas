package com.atlas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.atlas.model.OrderModel;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, Long> {
    Optional<OrderModel> findByOrderId(String orderId);
}