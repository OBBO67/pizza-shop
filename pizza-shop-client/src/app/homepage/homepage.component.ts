import { Component, OnInit } from "@angular/core";
import { User } from "@app/models/user";
import { AuthenticationService } from "@app/services/authentication.service";

@Component({
  selector: "app-homepage",
  templateUrl: "./homepage.component.html",
  styleUrls: ["./homepage.component.css"]
})
export class HomepageComponent implements OnInit {
  currentUser: User;

  constructor(private authService: AuthenticationService) {}

  ngOnInit(): void {
    this.currentUser = this.authService.currentUserValue;
  }

  get getUser(): User {
    this.currentUser = this.authService.currentUserValue;
    return this.currentUser;
  }
}
