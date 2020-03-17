import { Address } from "@app/models/customer-address";

export class User {
  id: number;
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  addresses: Array<Address>;
  token?: string; // optional

  constructor() {
    this.addresses = [];
  }
}
