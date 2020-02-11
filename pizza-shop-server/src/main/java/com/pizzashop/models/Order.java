package com.pizzashop.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "CUSTOMER_ORDER")
public class Order {

	private @Id @GeneratedValue Long id;
	private OrderStatus orderStatus;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "CUSTOMER_ID")
	private Customer customer; // one order per customer
	
	private Date placedAt;
	
	// Todo: add pizza object, this will be a ManyToOne relationship
	
	public Order() {}
	
	public Order(OrderStatus orderStatus, Customer customer) {
		this.orderStatus = orderStatus;
		this.customer = customer;
	}
	
	/*
	 * Hibernate will call this before persisting the order
	 */
	@PrePersist
	public void placedAt() {
		this.placedAt = new Date();
	}
}
