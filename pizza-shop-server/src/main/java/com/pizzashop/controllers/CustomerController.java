package com.pizzashop.controllers;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.pizzashop.data.CustomerRepository;
import com.pizzashop.exceptions.CustomerNotFoundException;
import com.pizzashop.modelassembler.CustomerModelAssembler;
import com.pizzashop.models.Customer;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class CustomerController {

	private CustomerRepository customerRepo;
	private CustomerModelAssembler assembler;
	
	public CustomerController(CustomerRepository customerRepo,
			CustomerModelAssembler assembler) {
		this.customerRepo = customerRepo;
		this.assembler = assembler;
	}
	
	/**
	 * Returns a customer in the body of a HTTP response as JSON.
	 * @param id	The id of the customer.
	 * @return		The customer
	 */
	@GetMapping("/customers/{id}")
	public EntityModel<Customer> getCustomer(@PathVariable Long id) {
		Customer customer = customerRepo.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException(id));
		
		log.debug("Found customer: " + customer.toString());
		
		return assembler.toModel(customer);
	}
}
