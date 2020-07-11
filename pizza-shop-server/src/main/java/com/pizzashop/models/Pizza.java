package com.pizzashop.models;

import java.util.List;

import com.pizzashop.models.ingredients.Ingredient;

import lombok.Data;

@Data
public abstract class Pizza {
	
	private String name;
	private List<Ingredient> ingredients;
	

}
