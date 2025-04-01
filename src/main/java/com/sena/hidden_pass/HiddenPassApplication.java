package com.sena.hidden_pass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HiddenPassApplication {

	public static void main(String[] args) {
		SpringApplication.run(HiddenPassApplication.class, args);
	}

}
