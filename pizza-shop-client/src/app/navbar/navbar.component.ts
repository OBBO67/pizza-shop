import { Component, OnInit } from "@angular/core";
import { AuthGuard } from "@app/interceptors/auth.guard";
import { AuthenticationService } from "@app/services/authentication.service";
import { User } from "@app/models/user";

@Component({
  selector: "app-navbar",
  templateUrl: "./navbar.component.html",
  styleUrls: ["./navbar.component.css"]
})
export class NavbarComponent implements OnInit {
  currentUser: User;

  constructor(private authService: AuthenticationService) {}

  ngOnInit(): void {
    this.currentUser = this.authService.currentUserValue;
  }
}
