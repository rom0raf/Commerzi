package com.commerzi.commerziapi;

import com.commerzi.commerziapi.address.CheckAddress;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommerziApiApplication {

	public static void main(String[] args) {
		CheckAddress.init();
		SpringApplication.run(CommerziApiApplication.class, args);
	}

}
