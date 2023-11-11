package org.yasz.praiseslowly;

import common.AsposeRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PraiseslowlyApplication {

	public static void main(String[] args) {
		AsposeRegister.registerAll();
		SpringApplication.run(PraiseslowlyApplication.class, args);
	}

}
