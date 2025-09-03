package com.example.design_problems.service;

import org.springframework.stereotype.Service;

@Service
public interface OrderService {

    String getOrders(String id);
}
