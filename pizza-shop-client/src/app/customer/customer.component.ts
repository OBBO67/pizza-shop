import { Component, OnInit } from "@angular/core";

import { Customer } from "@app/models/customer";
import { CustomerService } from "@app/customer.service";

@Component({
  selector: "app-customer",
  templateUrl: "./customer.component.html",
  styleUrls: ["./customer.component.css"]
})
export class CustomerComponent implements OnInit {
  customer: Customer;
  id: number;

  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    this.getCustomer();
  }

  // TODO: pass the customer id by parameter
  getCustomer(): void {
    if (this.id !== null && this.id !== undefined) {
      this.customerService.getCustomerById(1).subscribe(customer => {
        this.customer = customer;
        console.log(customer);
      });
    }
  }

  onKey(event: KeyboardEvent) {
    this.id = +(event.target as HTMLInputElement).value;
  }
}
