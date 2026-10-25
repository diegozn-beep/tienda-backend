package com.diego.tiendabackend.controller;

import com.diego.tiendabackend.dto.OrderItemRequest;
import com.diego.tiendabackend.dto.OrderRequest;
import com.diego.tiendabackend.model.Order;
import com.diego.tiendabackend.model.OrderItem;
import com.diego.tiendabackend.model.Product;
import com.diego.tiendabackend.model.User;
import com.diego.tiendabackend.repository.OrderRepository;
import com.diego.tiendabackend.repository.ProductRepository;
import com.diego.tiendabackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    @Transactional
    public Order create(@RequestBody OrderRequest request, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado.");
        }

        if (request.customerName == null || request.customerName.isBlank()
                || request.address == null || request.address.isBlank()
                || request.phone == null || request.phone.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Completa nombre, dirección y teléfono.");
        }

        if (request.items == null || request.items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pedido no tiene productos.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setCustomerName(request.customerName);
        order.setAddress(request.address);
        order.setPhone(request.phone);
        order.setPaymentMethod(request.paymentMethod);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        double total = 0;

        for (OrderItemRequest itemRequest : request.items) {
            Product product = productRepository.findById(itemRequest.productId).orElse(null);

            if (product == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Un producto del pedido no existe.");
            }
            if (itemRequest.quantity <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a 0.");
            }
            if (product.getStock() < itemRequest.quantity) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay stock suficiente de " + product.getName() + ".");
            }

            product.setStock(product.getStock() - itemRequest.quantity);
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setProductName(product.getName());
            item.setQuantity(itemRequest.quantity);
            item.setPrice(product.getPrice());
            order.getItems().add(item);

            total = total + product.getPrice() * itemRequest.quantity;
        }

        order.setTotal(total);
        return orderRepository.save(order);
    }

    @GetMapping("/my")
    public List<Order> myOrders(Authentication authentication) {
        return orderRepository.findByUserEmailOrderByIdDesc(authentication.getName());
    }
}