import { Address } from "./customer-address";

export interface Customer {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  addresses: Address[];
}
