package com.example.design_problems.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements  OrderService{
    @Override
    @CircuitBreaker(name="testCircuitBreaker", fallbackMethod = "testFallbackMethod")
    public String getOrders(String id) {
        System.out.println("order id "+id+" dispatched");
        return id;
    }
    public void testFallbackMethod(String id, Throwable e){
        System.out.println("test fallback");
    }
}
