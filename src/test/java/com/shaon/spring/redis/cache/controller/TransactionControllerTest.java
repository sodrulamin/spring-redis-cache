package com.shaon.spring.redis.cache.controller;

import com.google.gson.Gson;
import com.shaon.spring.redis.cache.dao.Order;
import com.shaon.spring.redis.cache.dao.OrderRepository;
import com.shaon.spring.redis.cache.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@TestPropertySource(properties = {
//        "spring.datasource.url=jdbc:tc:mysql:8.0.32:///testdb"
//})
class TransactionControllerTest {

    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.31")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeAll
    static void startContainer() {
        mysql.start();
        System.setProperty("spring.datasource.url", mysql.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysql.getUsername());
        System.setProperty("spring.datasource.password", mysql.getPassword());
    }

    @ParameterizedTest
    @CsvSource ({
            "Less than 100, 150, 99, false",
            "Greater than 1000, 150, 1001, false",
            "Greater than 100, 130, 101, true"
    })
    void orderSaveTest(String details, int price, int quantity, boolean isSaved) throws Exception {
        OrderDTO dto = new OrderDTO();
        Gson gson = new Gson();

        dto.setDetails(details);
        dto.setPrice(price);
        dto.setQty(quantity);

        String content = gson.toJson(dto);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/failed-transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isOk())
                .andReturn();

        content = result.getResponse().getContentAsString();
        log.info("Response: {}", content);
        dto = gson.fromJson(content, OrderDTO.class);

        Optional<Order> optional = orderRepository.findById(dto.getId());

        Assertions.assertEquals(isSaved, optional.isPresent());
    }
}