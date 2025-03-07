package com.shaon.spring.redis.cache.service;

import java.util.List;

import com.shaon.spring.redis.cache.OrderNotFoundException;
import com.shaon.spring.redis.cache.dao.Order;
import com.shaon.spring.redis.cache.dao.OrderRepository;
import com.shaon.spring.redis.cache.dto.OrderDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository repository;

	private final OrderMapper mapper;


	public OrderDTO save(OrderDTO dto) {
		repository.save(mapper.toOrder(dto));
		return dto;
	}

	@Cacheable(value = "order")
	public List<OrderDTO> findAll() {
		return mapper.toOrderDTOS(repository.findAll());
	}

	@Cacheable(value = "order", key = "#id", condition="#id>=10")
	public OrderDTO findOrderById(int id) {
		return mapper.toOrderDTO(repository.findById(id).
				orElseThrow(() -> new OrderNotFoundException("Not found")));
	}

	@Caching(
			  evict = {@CacheEvict(value = "order", allEntries = true), @CacheEvict(value="order", key="#id")
			}) 
	public String deleteOrder(int id) {
		repository.deleteById(id);
		return "Order deleted successfully!";
	}

	@Transactional
	public OrderDTO failedTransaction(OrderDTO dto) {

		Order order = mapper.toOrder(dto);


		repository.save(order);

		if(dto.getQty() > 1000)
			throw new RuntimeException("Test fail message");

		if(dto.getQty() < 100) {
			log.error("Quantity can not be less than 100");
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}

		return mapper.toOrderDTO(order);

	}
}
