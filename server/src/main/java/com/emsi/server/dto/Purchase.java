package com.emsi.server.dto;

import com.emsi.server.entity.Address;
import com.emsi.server.entity.Order;
import com.emsi.server.entity.OrderItem;
import com.emsi.server.entity.User;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {
    private User customer;
    private Address orderAddress;
    private Order order;
    private Set<OrderItem> orderItems;
}