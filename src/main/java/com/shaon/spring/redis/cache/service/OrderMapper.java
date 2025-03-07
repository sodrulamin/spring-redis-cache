package com.shaon.spring.redis.cache.service;

import com.shaon.spring.redis.cache.dao.Order;
import com.shaon.spring.redis.cache.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderMapper {
    public Order toOrder (OrderDTO dto) {
        Order order = new Order();

        order.setDetails(dto.getDetails());
        order.setPrice(dto.getPrice());
        order.setQty(dto.getQty());

        return order;
    }

    public List<OrderDTO> toOrderDTOS(List<Order> list) {
        List<OrderDTO> result = new ArrayList<>();

        if(list == null)
            return result;

        for(Order order: list)
            result.add(toOrderDTO(order));

        return result;
    }

    public OrderDTO toOrderDTO(Order dao) {
        OrderDTO dto = new OrderDTO();

        dto.setId(dao.getId());
        dto.setDetails(dao.getDetails());
        dto.setPrice(dao.getPrice());
        dto.setQty(dao.getQty());

        return dto;
    }
}
