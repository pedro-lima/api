package com.minsait.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("random"));
		SpringApplication.run(ApiApplication.class, args);
	}

}
