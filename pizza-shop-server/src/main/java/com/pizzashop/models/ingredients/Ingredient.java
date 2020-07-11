package com.pizzashop.models.ingredients;

import lombok.Data;

@Data
public abstract class Ingredient {
	
//	private IngredientType type;
	private String name;
	private Integer stockCount;

}
