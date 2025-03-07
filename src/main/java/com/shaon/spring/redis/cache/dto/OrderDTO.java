package com.shaon.spring.redis.cache.dto;

import lombok.Data;

@Data
public class OrderDTO {
    private int id;

    private String details;

    private int qty;

    private long price;
}
