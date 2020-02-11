package com.pizzashop.models;

import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
public class CustomerAddress {
	
	private String houseNumber;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String postcode;
	
	public CustomerAddress() {}

	public CustomerAddress(String houseNumber, String addressLine1, 
			String addressLine2, String city, String postcode) {
		this.houseNumber = houseNumber;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.postcode = postcode;
	}

}
