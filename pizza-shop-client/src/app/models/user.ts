import { Customer } from "@app/models/customer";
import { Address } from "@app/models/customer-address";

export interface User {
  id: number;
  username: string;
  password: string;
  firstName: string;
  lastName: string;
  address: Address;
  token?: string; // optional
}
