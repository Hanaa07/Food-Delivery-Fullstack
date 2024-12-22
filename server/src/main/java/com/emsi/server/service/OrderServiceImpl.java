package com.emsi.server.service;

import com.emsi.server.dto.Purchase;
import com.emsi.server.dto.PurchaseResponse;
import com.emsi.server.entity.Order;
import com.emsi.server.entity.OrderItem;
import com.emsi.server.entity.User;
import com.emsi.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.UUID;

@Service
public class OrderServiceImpl  implements OrderService {
    private UserRepository userRepository;

    public OrderServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {

        Order order = purchase.getOrder();
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrder_tracking_number(orderTrackingNumber);
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(item -> order.add(item));
        order.setAddress(purchase.getOrderAddress());
        User user = purchase.getCustomer();
        String theEmail = user.getEmail();
        User userDB = userRepository.findByEmail(theEmail);
        if(userDB!=null)
        {
            user=userDB;
        }
        user.add(order);
        userRepository.save(user);
        return new PurchaseResponse(orderTrackingNumber);
    }
}