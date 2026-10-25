package com.diego.tiendabackend.repository;

import com.diego.tiendabackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserEmailOrderByIdDesc(String email);
}