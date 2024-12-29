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
public class OrderRepositoryTest {

    private final Logger log = LoggerFactory.getLogger(OrderRepositoryTest.class);

    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private OrderItemRepository orderItemRepo;
    @Autowired
    private AddressRepository addressRepo;
    @Autowired
    private StateRepository stateRepo;
    @Autowired
    private UserRepository userRepo;

    @BeforeEach
    void setUp() {
        // Create and save a User if it doesn't exist
        if (!userRepo.findAll().iterator().hasNext()) {
            User user = User.builder().name("Test User").build();
            userRepo.save(user);
        }

        // Create and save a State if it doesn't exist
        if (!stateRepo.findAll().iterator().hasNext()) {
            State state = State.builder().name("Śląskie").build();
            stateRepo.save(state);
        }

        // Create and save an Address if it doesn't exist
        if (!addressRepo.findAll().iterator().hasNext()) {
            Address address = Address.builder()
                    .state(stateRepo.findByName("Śląskie").orElseThrow(() -> new RuntimeException("State not found")))
                    .city("Tarnowskie Góry")
                    .street("Kwiatowa 5/2")
                    .zip_code("42-606")
                    .build();
            addressRepo.save(address);
        }

        // Create and save an Order if it doesn't exist
        Order order;
        if (!orderRepo.findAll().iterator().hasNext()) {
            User user = userRepo.findAll().iterator().next();
            Address address = addressRepo.findAll().iterator().next();
            order = new Order();
            order.setUser(user);
            order.setAddress(address);
            order.setDate(new Date());
            order.setStatus(true);
            order.setOrder_tracking_number("setupOrder123");
            orderRepo.save(order);
        } else {
            order = orderRepo.findAll().iterator().next();
        }

        // Create and save an OrderItem associated with the Order if it doesn't exist
        if (!orderItemRepo.findAll().iterator().hasNext()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct_id(1);
            orderItem.setQuantity(10);
            orderItem.setUnit_price(100);
            orderItem.setOrder(order); // Associate the OrderItem with the Order
            orderItemRepo.save(orderItem);
        }
    }


    @Test
    @Transactional
    public void insertAndCheckOrder() {
        // Retrieve required entities
        User user = userRepo.findAll().iterator().next();
        Address address = addressRepo.findAll().iterator().next();
        OrderItem orderItem = orderItemRepo.findAll().iterator().next();

        // Create a new Order
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setOrderItems(new HashSet<>(Set.of(orderItem))); // Use a mutable HashSet
        order.setDate(new Date());
        order.setOrder_tracking_number("order12345");
        order.setStatus(true);
        order.setTotal_price(1000);
        order.setTotal_quantity(100);

        // Save the Order
        Order savedOrder = orderRepo.save(order);

        // Log and assert the saved Order
        log.info("{} | {} | {} | {} | {} | {} | {} | {}",
                savedOrder.getId(), savedOrder.getTotal_price(), savedOrder.getTotal_quantity(),
                savedOrder.getOrderItems(), savedOrder.getAddress(), savedOrder.getDate(),
                savedOrder.getOrder_tracking_number(), savedOrder.getUser());

        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getTotal_price()).isEqualTo(1000);
        assertThat(savedOrder.getTotal_quantity()).isEqualTo(100);
        assertThat(savedOrder.getAddress()).isEqualTo(address);

        // Update the Order
        savedOrder.setTotal_price(500);
        savedOrder.setTotal_quantity(50);
        savedOrder.setOrder_tracking_number("updated12345");
        savedOrder.setStatus(false);
        Order updatedOrder = orderRepo.save(savedOrder);

        // Log and assert the updated Order
        log.info("{} | {} | {} | {} | {} | {} | {} | {}",
                updatedOrder.getId(), updatedOrder.getTotal_price(), updatedOrder.getTotal_quantity(),
                updatedOrder.getOrderItems(), updatedOrder.getAddress(), updatedOrder.getDate(),
                updatedOrder.getOrder_tracking_number(), updatedOrder.getUser());

        assertThat(updatedOrder.getTotal_price()).isEqualTo(500);
        assertThat(updatedOrder.getTotal_quantity()).isEqualTo(50);
        assertThat(updatedOrder.getOrder_tracking_number()).isEqualTo("updated12345");
        assertThat(updatedOrder.isStatus()).isFalse();

        // Delete the Order and verify
        Long orderId = updatedOrder.getId();
        orderRepo.delete(updatedOrder);

        log.info("Object deleted from repository");

        Optional<Order> searchById = orderRepo.findById(orderId);
        assertThat(searchById).isEmpty();
        log.info("Object not found. End of test!");
    }
}
