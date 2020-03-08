package com.pizzashop.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pizzashop.auth.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByUsername(String username);

}
