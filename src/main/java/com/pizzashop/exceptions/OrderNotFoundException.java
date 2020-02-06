package com.pizzashop.exceptions;

public class OrderNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OrderNotFoundException(Long id) {
		super("Could not find employee with id: " + id);
	}
}
