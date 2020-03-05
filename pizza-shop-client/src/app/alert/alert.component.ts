import { Component, OnInit } from "@angular/core";
import { Subscription } from "rxjs";

/*
 * Alert component passes alert messages to the template whenever
 * a message is recieved from the alert service. It does this by
 * subscribing to the alerts service getAlert() method which
 * returns an Observable.
 */

@Component({
  selector: "app-alert",
  templateUrl: "./alert.component.html",
  styleUrls: ["./alert.component.css"]
})
export class AlertComponent implements OnInit {
  private subscription: Subscription;
  message: any;

  constructor(private alertService: AlertService) {}

  ngOnInit(): void {
    // on init subscribe to the AlertService and
    // when a message is recieved then display
    // approriate alert type
    this.subscription = this.alertService.getAlert().subscribe(message => {
      switch (message && message.type) {
        case "success":
          message.cssClass = "alert alert-success";
          break;
        case "error":
          message.cssClass = "alert alert-danger";
          break;
      }

      this.message = message;
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
