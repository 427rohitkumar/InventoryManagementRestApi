package com.inventry.inventry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventryApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventryApplication.class, args);
		System.out.println("Application is running...");
	}

}
