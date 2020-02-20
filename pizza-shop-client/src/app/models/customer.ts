import { Address } from "@app/models/customer-address";

export interface Customer {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  addresses: Address[];
}
