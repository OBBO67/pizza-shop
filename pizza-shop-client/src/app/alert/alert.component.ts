import { Component, OnInit } from "@angular/core";

/*
 * Alert component passes alert messages to the template whenever
 * a message is recieved from the alert service. It does this by
 * subscribing to the alerts service getMessage() method which
 * returns an Observable.
 */

@Component({
  selector: "app-alert",
  templateUrl: "./alert.component.html",
  styleUrls: ["./alert.component.css"]
})
export class AlertComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
