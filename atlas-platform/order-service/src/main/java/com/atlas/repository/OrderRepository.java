package com.atlas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.atlas.model.OrderModel;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, String> {
}