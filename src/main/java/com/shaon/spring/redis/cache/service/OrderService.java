package com.shaon.spring.redis.cache.service;

import java.util.List;

import com.shaon.spring.redis.cache.OrderNotFoundException;
import com.shaon.spring.redis.cache.dao.Order;
import com.shaon.spring.redis.cache.dao.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


@Service
@Slf4j
public class OrderService {

	@Autowired
	private OrderRepository repository;

	public Order save(Order order) {
		repository.save(order);
		return order;
	}

	@Cacheable(value = "order")
	public List<Order> findAll() {
		return repository.findAll();
	}

	@Cacheable(value = "order", key = "#id", condition="#id>=10")
	public Order findOrderById(int id) {
		return repository.findById(id).orElseThrow(() -> new OrderNotFoundException("Not found"));
	}

	@Caching(
			  evict = {@CacheEvict(value = "order", allEntries = true), @CacheEvict(value="order", key="#id")
			}) 
	public String deleteOrder(int id) {
		repository.deleteById(id);
		return "Order deleted successfully!";
	}

	@Transactional
	public Order failedTransaction(Order order) {
		repository.save(order);

		if(order.getQty() > 1000)
			throw new RuntimeException("Test fail message");

		if(order.getQty() < 100) {
			log.error("Quantity can not be less than 100");
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}

		return order;

	}
}
