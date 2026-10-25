package com.diego.tiendabackend.dto;

import java.util.List;

public class OrderRequest {
    public String customerName;
    public String address;
    public String phone;
    public String paymentMethod;
    public List<OrderItemRequest> items;
}