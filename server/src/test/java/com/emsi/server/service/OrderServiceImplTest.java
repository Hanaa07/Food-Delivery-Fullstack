package com.emsi.server.service;

import com.emsi.server.dto.Purchase;
import com.emsi.server.dto.PurchaseResponse;
import com.emsi.server.entity.Address;
import com.emsi.server.entity.Order;
import com.emsi.server.entity.OrderItem;
import com.emsi.server.entity.User;
import com.emsi.server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User mockUser;
    private Order mockOrder;
    private Address mockAddress;
    private OrderItem mockOrderItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mock entities
        mockUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .name("Test User")
                .build();

        mockOrder = new Order();
        mockOrder.setId(1L);

        mockAddress = Address.builder()
                .id(1L)
                .city("Test City")
                .street("Test Street")
                .zip_code("12345")
                .build();

        mockOrderItem = new OrderItem();
        mockOrderItem.setId(1L);
        mockOrderItem.setProduct_id(101);
        mockOrderItem.setQuantity(2);
        mockOrderItem.setUnit_price(50);

        // Set up relationships
        Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(mockOrderItem);

        mockOrder.setOrderItems(orderItems);
        mockOrder.setAddress(mockAddress);

        mockUser.add(mockOrder);
    }

    @Test
    void testPlaceOrder_NewUser() {
        // Mock purchase
        Purchase purchase = new Purchase();
        purchase.setOrder(mockOrder);
        purchase.setOrderItems(mockOrder.getOrderItems());
        purchase.setOrderAddress(mockOrder.getAddress());
        purchase.setCustomer(mockUser);

        // Mock repository behavior
        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(null);
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        // Call the service method
        PurchaseResponse response = orderService.placeOrder(purchase);

        // Verify interactions and assert results
        verify(userRepository, times(1)).findByEmail(mockUser.getEmail());
        verify(userRepository, times(1)).save(mockUser);
        assertThat(response.getOrderTrackingNumber()).isNotNull();
    }

    @Test
    void testPlaceOrder_ExistingUser() {
        // Mock existing user in the database
        User existingUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .name("Existing User")
                .build();

        // Mock purchase
        Purchase purchase = new Purchase();
        purchase.setOrder(mockOrder);
        purchase.setOrderItems(mockOrder.getOrderItems());
        purchase.setOrderAddress(mockOrder.getAddress());
        purchase.setCustomer(mockUser);

        // Mock repository behavior
        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(existingUser);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Call the service method
        PurchaseResponse response = orderService.placeOrder(purchase);

        // Verify interactions and assert results
        verify(userRepository, times(1)).findByEmail(mockUser.getEmail());
        verify(userRepository, times(1)).save(existingUser);
        assertThat(response.getOrderTrackingNumber()).isNotNull();
    }

//    @Test
//    void testPlaceOrder_NullOrder() {
//        // Mock purchase with null order
//        Purchase purchase = new Purchase();
//        purchase.setOrder(null);
//        purchase.setCustomer(mockUser);
//
//        try {
//            orderService.placeOrder(purchase);
//        } catch (NullPointerException e) {
//            assertThat(e).hasMessageContaining("Order must not be null");
//        }
//    }
//
//    @Test
//    void testGenerateOrderTrackingNumber() {
//        // Call the private method using reflection
//        String trackingNumber = orderService.placeOrder(
//                new Purchase(mockOrder, mockUser, mockOrder.getOrderItems(), mockOrder.getAddress())
//        ).getOrderTrackingNumber();
//
//        assertThat(trackingNumber).isNotEmpty();
//    }
}
