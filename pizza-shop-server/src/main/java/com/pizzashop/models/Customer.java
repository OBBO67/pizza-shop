package com.pizzashop.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class Customer {
	
	private @Id @GeneratedValue Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	
	
	/**
	 * A customer can have more than one address.
	 */
	@OneToMany
	private CustomerAddress address;
	
	public Customer() {}
	
	public Customer(String firstName, String lastName,
			String email, String password, CustomerAddress address) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.address = address;
	}

}
