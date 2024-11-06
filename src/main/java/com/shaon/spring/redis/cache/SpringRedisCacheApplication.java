package com.shaon.spring.redis.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringRedisCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRedisCacheApplication.class, args);
	}

}
