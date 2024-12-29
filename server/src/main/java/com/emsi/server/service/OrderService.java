package com.emsi.server.service;

import com.emsi.server.dto.Purchase;
import com.emsi.server.dto.PurchaseResponse;

public interface OrderService {
    PurchaseResponse placeOrder(Purchase purchase);
}
