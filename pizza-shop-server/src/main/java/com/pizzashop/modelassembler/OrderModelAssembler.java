package com.pizzashop.modelassembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.stereotype.Component;

import com.pizzashop.controllers.OrderController;
import com.pizzashop.models.Order;
import com.pizzashop.models.OrderStatus;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

	@Override
	public EntityModel<Order> toModel(Order order) {
		
		// Unconditional links to single-item resource and aggregate root
		EntityModel<Order> orderResource = new EntityModel<>(order,
				linkTo(methodOn(OrderController.class).getOrder(order.getId())).withSelfRel(),
				linkTo(methodOn(OrderController.class).getAllOrders()).withRel("orders")
				);
		
		// Conditional links based on state of the order
		if (order.getOrderStatus() == OrderStatus.IN_PROGRESS) {
			orderResource.add(
					linkTo(methodOn(OrderController.class)
							.cancelOrder(order.getId())).withRel("cancel"));
			orderResource.add(
					linkTo(methodOn(OrderController.class)
							.completeOrder(order.getId())).withRel("complete"));
		}
		
		return orderResource;
	}

}
