package com.pizzashop.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pizzashop.data.CustomerRepository;
import com.pizzashop.models.Customer;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
class DatabaseLoader {

  @Bean
  CommandLineRunner initDatabase(CustomerRepository repository) {
    return args -> {
      log.info("Preloading " + repository.save(
    		  new Customer("Bilbo", "Baggins", "bilbo@aol.com", "password")));
    };
  }
}
