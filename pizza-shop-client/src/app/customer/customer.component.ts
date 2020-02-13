import { Component, OnInit } from "@angular/core";
import { Customer } from "../customer";
import { CustomerService } from "../customer.service";

@Component({
  selector: "app-customer",
  templateUrl: "./customer.component.html",
  styleUrls: ["./customer.component.css"]
})
export class CustomerComponent implements OnInit {
  customer: Customer;

  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    this.getCustomer();
  }

  // TODO: pass the customer id by parameter
  getCustomer(): void {
    this.customerService
      .getCustomerById(1)
      .subscribe(customer => (this.customer = customer));
  }
}
