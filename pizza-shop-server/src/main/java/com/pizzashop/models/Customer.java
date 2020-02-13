package com.pizzashop.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
	@OneToMany()
	@Cascade(CascadeType.ALL)
	private List<CustomerAddress> addresses;
	
	public Customer() {}
	
	public Customer(String firstName, String lastName,
			String email, String password, List<CustomerAddress> addresses) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.addresses = addresses;
	}

}
