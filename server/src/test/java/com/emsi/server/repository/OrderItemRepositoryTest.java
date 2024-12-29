package com.emsi.server.repository;

import com.emsi.server.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class OrderItemRepositoryTest {

    private final Logger log = LoggerFactory.getLogger(OrderItemRepositoryTest.class);

    @Autowired
    private OrderItemRepository orderItemRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AddressRepository addressRepo;

    private User savedUser;
    private Address savedAddress;

    @BeforeEach
    void setUp() {
        // Create and save User
        User user = User.builder().name("User").build();
        savedUser = userRepo.save(user);

        // Create and save Address
        Address address = Address.builder().street("Street").city("City").build();
        savedAddress = addressRepo.save(address);
    }

    @Test
    @Transactional
    public void insertAndCheckOrderItem() {
        // Ensure Order exists or create one
        Optional<Order> existingOrder = orderRepo.findById(1L);
        Order order1;
        if (existingOrder.isEmpty()) {
            order1 = new Order();
            order1.setUser(savedUser); // Use the saved User
            order1.setAddress(savedAddress); // Use the saved Address
            order1.setDate(new Date());
            order1.setOrder_tracking_number("12345-ABCDE");
            order1.setStatus(true);
            order1.setTotal_price(1000);
            order1.setTotal_quantity(10);
            order1 = orderRepo.save(order1);
        } else {
            order1 = existingOrder.get();
        }

        // Ensure another Order exists or create one
        Optional<Order> secondOrder = orderRepo.findById(2L);
        Order order2;
        if (secondOrder.isEmpty()) {
            order2 = new Order();
            order2.setUser(savedUser); // Use the saved User
            order2.setAddress(savedAddress); // Use the saved Address
            order2.setDate(new Date());
            order2.setOrder_tracking_number("67890-FGHIJ");
            order2.setStatus(true);
            order2.setTotal_price(2000);
            order2.setTotal_quantity(20);
            order2 = orderRepo.save(order2);
        } else {
            order2 = secondOrder.get();
        }

        // Ensure OrderItem exists or create one
        Optional<OrderItem> existingOrderItem = orderItemRepo.findById(1L);
        OrderItem orderItem;
        if (existingOrderItem.isEmpty()) {
            orderItem = new OrderItem();
            orderItem.setQuantity(100);
            orderItem.setUnit_price(1000);
            orderItem.setOrder(order1); // Associate with the first Order
            orderItem.setProduct_id(1);
            orderItem = orderItemRepo.save(orderItem);
        } else {
            orderItem = existingOrderItem.get();
        }

        // Assertions for initial OrderItem
        log.info("{} | {} | {} | {}", orderItem.getId(), orderItem.getUnit_price(), orderItem.getQuantity(), orderItem.getOrder());
        assertThat(orderItem.getId()).isNotNull();
        assertThat(orderItem.getUnit_price()).isEqualTo(1000);
        assertThat(orderItem.getQuantity()).isEqualTo(100);

        // Modify and re-save OrderItem
        orderItem.setQuantity(9);
        orderItem.setUnit_price(90);
        orderItem.setOrder(order2); // Associate with the second Order
        orderItem.setProduct_id(2);
        OrderItem updatedOrderItem = orderItemRepo.save(orderItem);

        // Assertions for updated OrderItem
        log.info("{} | {} | {} | {}", updatedOrderItem.getId(), updatedOrderItem.getUnit_price(), updatedOrderItem.getQuantity(), updatedOrderItem.getOrder());
        assertThat(updatedOrderItem.getId()).isNotNull();
        assertThat(updatedOrderItem.getUnit_price()).isEqualTo(90);
        assertThat(updatedOrderItem.getQuantity()).isEqualTo(9);

        // Clean up by deleting the OrderItem
        Long orderItemId = updatedOrderItem.getId();
        orderItemRepo.delete(updatedOrderItem);
        log.info("Object deleted from repository");

        // Verify deletion
        Optional<OrderItem> searchById = orderItemRepo.findById(orderItemId);
        assertThat(searchById).isEmpty();
        log.info("Object not found. End of test!");
    }
}
