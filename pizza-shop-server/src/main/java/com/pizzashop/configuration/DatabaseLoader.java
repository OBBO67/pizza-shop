package com.pizzashop.configuration;

import static com.pizzashop.security.ApplicationUserRole.*;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pizzashop.auth.User;
import com.pizzashop.data.UserRepository;
import com.pizzashop.models.Customer;
import com.pizzashop.models.UserAddress;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
class DatabaseLoader {
	
	@Bean
	CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		
		User newUser = new User("Bilbo101",
				passwordEncoder.encode("password"),
				CUSTOMER.getGrantedAuthorities(), 
				"Bilbo",
				"Baggins",
				"bilbo@aol.com",
				Arrays.asList(new UserAddress("test", "test", "test", "test", "test")));
		
		return args -> {
			log.info("Preloading " + userRepository.save(newUser));
		};
	}
}
