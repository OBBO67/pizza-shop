package com.pizzashop.controllers;

import static com.pizzashop.security.ApplicationUserRole.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pizzashop.auth.User;
import com.pizzashop.data.UserRepository;
import com.pizzashop.exceptions.UserNotFoundException;
import com.pizzashop.modelassembler.UserModelAssembler;
import com.pizzashop.models.Customer;
import com.pizzashop.models.UserAddress;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

	private final UserRepository userRepository;
	private UserModelAssembler assembler;
	private final PasswordEncoder passwordEncoder;
	
	public UserController(UserRepository userRepository, UserModelAssembler assembler, 
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.assembler = assembler;
		this.passwordEncoder = passwordEncoder;
	}
	
	@GetMapping("/users/{id}")
	public EntityModel<User> getUser(@PathVariable Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(id));
		
		log.debug("Found customer: " + user.toString());
		
		return assembler.toModel(user);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<User> newUser(@RequestBody User user) {
		User userExists = userRepository.findByUsername(user.getUsername());
		
		if (userExists != null) {
			
			log.debug("User with username already exists");
			
			throw new BadCredentialsException("User with username " + 
					userExists.getUsername() + " already exists");
		}
		
		List<UserAddress> newAddresses = new ArrayList<>();
		
		UserAddress userAddress = null;
		
		if (user.getAddresses() != null && user.getAddresses().size() > 0) {
			userAddress = new UserAddress(
					user.getAddresses().get(0).getHouseNumber(), 
					user.getAddresses().get(0).getAddressLine1(), 
					user.getAddresses().get(0).getAddressLine2(), 
					user.getAddresses().get(0).getCity(), 
					user.getAddresses().get(0).getPostcode());
		}
		
		newAddresses.add(userAddress);
		
		User newUser = new User(user.getUsername(), 
				passwordEncoder.encode(user.getPassword()), CUSTOMER.getGrantedAuthorities(),
				user.getFirstName(), user.getLastName(), user.getEmail(), newAddresses);
		
		userRepository.save(newUser);
		return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
	}
	
}
