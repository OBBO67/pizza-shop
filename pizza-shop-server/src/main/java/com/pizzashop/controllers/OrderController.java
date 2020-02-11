package com.pizzashop.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	public ResponseEntity<EntityModel<Order>> newOrder(@RequestBody Order order) {
		
		order.setOrderStatus(OrderStatus.IN_PROGRESS);
		Order newOrder = orderRepo.save(order);
		
		return ResponseEntity
				.created(linkTo(methodOn(OrderController.class)
						.getOrder(newOrder.getId())).toUri())
				.body(assembler.toModel(newOrder));
	}
	
	/**
	 * Deletes an order by id. If the order is IN_PROGRESS then it can be cancelled. Otherwise,
	 * the order is not in a state that can be cancelled i.e its either COMPLETED or already
	 * CANCELLED.
	 * @param id	The id of the order to cancel.
	 * @return		Either the order which was cancelled or a VndError.
	 */
	@DeleteMapping(path = "/{id}/cancel")
	public ResponseEntity<RepresentationModel<?>> cancelOrder(@PathVariable Long id) {
		
		Order order = orderRepo.findById(id)
				.orElseThrow(() -> new OrderNotFoundException(id));
		
		if (order.getOrderStatus() == OrderStatus.IN_PROGRESS) {
			order.setOrderStatus(OrderStatus.CANCELLED);
			return ResponseEntity.ok(assembler.toModel(orderRepo.save(order)));
		}
		
		return ResponseEntity
				.status(HttpStatus.METHOD_NOT_ALLOWED)
				.body(new VndErrors.VndError("Method not allowed", 
						"You can't cancel an order that is in the " + order.getOrderStatus() 
						+ " status"));
	}
	
	/**
	 * Completes an order by id. If the order is IN_PROGRESS then it can be completed. Otherwise,
	 * the order is not in a state that can be complete i.e its either been CANCELLED or already
	 * COMPLETE.
	 * @param id	The id of the order to complete.
	 * @return		Either the order which was complete or a VndError.
	 */
	@PutMapping(path = "/{id}/complete")
	public ResponseEntity<RepresentationModel<?>> completeOrder(@PathVariable Long id) {
		
		Order order = orderRepo.findById(id)
				.orElseThrow(() -> new OrderNotFoundException(id));
		
		if (order.getOrderStatus() == OrderStatus.IN_PROGRESS) {
			order.setOrderStatus(OrderStatus.COMPLETED);
			return ResponseEntity.ok(assembler.toModel(orderRepo.save(order)));
		}
		
		return ResponseEntity
				.status(HttpStatus.METHOD_NOT_ALLOWED)
				.body(new VndErrors.VndError("Method not allowed", 
						"You can't complete an order that is in the " + order.getOrderStatus() 
						+ " status"));
	}
}
