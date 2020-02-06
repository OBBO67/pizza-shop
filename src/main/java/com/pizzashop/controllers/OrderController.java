package com.pizzashop.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pizzashop.data.OrderRepository;
import com.pizzashop.exceptions.OrderNotFoundException;
import com.pizzashop.modelassembler.OrderModelAssembler;
import com.pizzashop.models.Order;
import com.pizzashop.models.OrderStatus;

@RestController
@RequestMapping(path = "orders")
public class OrderController {
	
	OrderRepository orderRepo;
	OrderModelAssembler assembler;
	
	public OrderController(OrderRepository orderRepo,
			OrderModelAssembler assembler) {
		this.orderRepo = orderRepo;
		this.assembler = assembler;
	}
	
	/**
	 * Returns all the orders.
	 * @return		All orders.
	 */
	@GetMapping(path = "/")
	public CollectionModel<EntityModel<Order>> getAllOrders() {
		
		List<EntityModel<Order>> orders = orderRepo.findAll().stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());
		
		return new CollectionModel<>(orders, 
				linkTo(methodOn(OrderController.class).getAllOrders()).withSelfRel());
	}
	
	/**
	 * Returns an existing order in the body of a HTTP response as JSON.
	 * @param id	The order id.
	 * @return		The order.
	 */
	@GetMapping(path = "/{id}")
	public EntityModel<Order> getOrder(@PathVariable Long id) {
		
		return assembler.toModel(
				orderRepo.findById(id)
				.orElseThrow(() -> new OrderNotFoundException(id)));
	}
	
	/**
	 * Creates a new order and sets it's order status to IN_PROGRESS.
	 * Create a HTTP response with a CREATED status and a location header
	 * set to the new the order URI.
	 * @param order		The new order.
	 * @return			The HTTP response.
	 */
	@PostMapping(path = "/")
	ResponseEntity<EntityModel<Order>> newOrder(@RequestBody Order order) {
		
		order.setOrderStatus(OrderStatus.IN_PROGRESS);
		Order newOrder = orderRepo.save(order);
		
		return ResponseEntity
				.created(linkTo(methodOn(OrderController.class)
						.getOrder(newOrder.getId())).toUri())
				.body(assembler.toModel(newOrder));
	}
}
