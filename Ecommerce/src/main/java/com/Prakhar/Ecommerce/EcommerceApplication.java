package com.Prakhar.Ecommerce;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

    @Bean
    CommandLineRunner test(Environment env) {
        return args -> {
            System.out.println("DB = " + env.getProperty("DB_USERNAME"));
            System.out.println("TEST = " + env.getProperty("my.test"));
        };

    }

}
