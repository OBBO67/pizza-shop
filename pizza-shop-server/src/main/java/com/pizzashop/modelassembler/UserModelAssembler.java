package com.pizzashop.modelassembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.pizzashop.controllers.UserController;
import com.pizzashop.models.User;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

	public EntityModel<User> toModel(User user) {
		return new EntityModel<>(user,
				linkTo(methodOn(UserController.class)
						.getUser(user.getId())).withSelfRel());
	}
	
}
