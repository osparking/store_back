package com.bumsoap.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BumSoap {

	public static void main(String[] args) {
		SpringApplication.run(BumSoap.class, args);
	}

}
