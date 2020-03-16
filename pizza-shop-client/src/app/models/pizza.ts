import { Money } from "./money";

export interface Pizza {
  name: string;
  toppings: Topping[];
  description: string;
  price: Money;
}
