package com.pizzashop.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Customer {
	
	private @Id @GeneratedValue Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	
	// Todo: Add customer address
	
	public Customer() {}
	
	public Customer(String firstName, String lastName,
			String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

}
