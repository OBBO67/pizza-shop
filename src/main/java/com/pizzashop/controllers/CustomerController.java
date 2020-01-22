package com.pizzashop.controllers;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.pizzashop.data.CustomerRepository;
import com.pizzashop.exceptions.CustomerNotFoundException;
import com.pizzashop.modelassembler.CustomerResourceAssembler;
import com.pizzashop.models.Customer;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class CustomerController {

	private CustomerRepository customerRepo;
	private CustomerResourceAssembler assembler;
	
	public CustomerController(CustomerRepository customerRepo,
			CustomerResourceAssembler assembler) {
		this.customerRepo = customerRepo;
		this.assembler = assembler;
	}
	
	@GetMapping("/customers/{id}")
	public EntityModel<Customer> one(@PathVariable Long id) {
		Customer customer = customerRepo.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException(id));
		
		log.debug("Found customer: " + customer.toString());
		
		return assembler.toModel(customer);
	}
}
