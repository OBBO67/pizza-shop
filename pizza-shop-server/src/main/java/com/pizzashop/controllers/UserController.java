package com.pizzashop.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pizzashop.data.UserRepository;
import com.pizzashop.models.Customer;
import com.pizzashop.models.CustomerAddress;
import com.pizzashop.models.User;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/users")
@Log4j2
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	
	public UserController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<User> newUser(@RequestBody User user) {
		User userExists = userRepo.findByUsername(user.getUsername());
		
		if (userExists != null) {
			
			log.debug("User with username already exists");
			
			throw new BadCredentialsException("User with username " + 
					userExists.getUsername() + " already exists");
		}
		
		CustomerAddress customerAddress = new CustomerAddress(
				user.getCustomer().getAddresses().get(0).getHouseNumber(), 
				user.getCustomer().getAddresses().get(0).getAddressLine1(), 
				user.getCustomer().getAddresses().get(0).getAddressLine2(), 
				user.getCustomer().getAddresses().get(0).getCity(), 
				user.getCustomer().getAddresses().get(0).getPostcode());
		
		List<CustomerAddress> newAddresses = new ArrayList<>();
		
		newAddresses.add(customerAddress);
		
		Customer newCustomer = new Customer(user.getCustomer().getFirstName(), 
				user.getCustomer().getLastName(), 
				user.getCustomer().getEmail(), 
				newAddresses);
		
		User newUser = new User(user.getUsername(), 
				passwordEncoder.encode(user.getPassword()), 
				newCustomer);
		
		userRepo.save(newUser);
		return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
	}
	
}
