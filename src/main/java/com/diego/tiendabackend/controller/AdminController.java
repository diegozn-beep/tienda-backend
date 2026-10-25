package com.diego.tiendabackend.controller;

import com.diego.tiendabackend.model.Order;
import com.diego.tiendabackend.model.Product;
import com.diego.tiendabackend.repository.OrderRepository;
import com.diego.tiendabackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/orders")
    public List<Order> allOrders() {
        return orderRepository.findAll();
    }

    @PutMapping("/orders/{id}/status")
    public Order changeStatus(@PathVariable Long id, @RequestParam String status) {
        if (!status.equals("PENDING") && !status.equals("SHIPPED") && !status.equals("DELIVERED")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado no válido. Usa PENDING, SHIPPED o DELIVERED.");
        }

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado.");
        }

        order.setStatus(status);
        return orderRepository.save(order);
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        double sales = 0;
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            sales = sales + order.getTotal();
        }

        int lowStock = 0;
        for (Product product : productRepository.findAll()) {
            if (product.getStock() <= 3) {
                lowStock++;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalSales", sales);
        result.put("totalOrders", orders.size());
        result.put("lowStockProducts", lowStock);
        return result;
    }
}