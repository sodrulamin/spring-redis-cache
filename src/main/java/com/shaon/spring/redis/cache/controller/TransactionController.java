package com.shaon.spring.redis.cache.controller;

import com.shaon.spring.redis.cache.dao.Order;
import com.shaon.spring.redis.cache.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final OrderService orderService;


    @PostMapping("/failed-transaction")
    public Order failedTransaction(@RequestBody Order order) {

        try {
            order = orderService.failedTransaction(order);
        }
        catch (Exception e) {
            log.error("Exception", e);
        }
        return order;
    }
}
