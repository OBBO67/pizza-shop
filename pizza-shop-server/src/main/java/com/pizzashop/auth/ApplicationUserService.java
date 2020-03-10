package com.pizzashop.auth;

import static com.pizzashop.security.ApplicationUserRole.CUSTOMER;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pizzashop.data.UserRepository;
import com.pizzashop.models.UserAddress;

@Service
public class ApplicationUserService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	@Autowired
	public ApplicationUserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		
		return user;
	}

}
