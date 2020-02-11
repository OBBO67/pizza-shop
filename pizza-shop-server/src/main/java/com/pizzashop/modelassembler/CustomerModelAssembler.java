package com.pizzashop.modelassembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.pizzashop.controllers.CustomerController;
import com.pizzashop.models.Customer;

@Component
public class CustomerModelAssembler implements RepresentationModelAssembler<Customer, EntityModel<Customer>> {

	@Override
	public EntityModel<Customer> toModel(Customer customer) {
		return new EntityModel<>(customer,
				linkTo(methodOn(CustomerController.class)
						.getCustomer(customer.getId())).withSelfRel());
	}
	
}
