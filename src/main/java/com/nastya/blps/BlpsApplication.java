package com.nastya.blps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.nastya.blps.model")
public class BlpsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlpsApplication.class, args);
	}

	

}
