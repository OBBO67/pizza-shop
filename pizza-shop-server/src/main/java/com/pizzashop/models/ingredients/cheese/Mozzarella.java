package com.pizzashop.models.ingredients.cheese;

import com.pizzashop.models.ingredients.Ingredient;
import com.pizzashop.models.ingredients.IngredientType;

import lombok.Data;

@Data
public class Mozzarella extends Ingredient {
	
	private static final IngredientType TYPE = IngredientType.CHEESE;
	

}
