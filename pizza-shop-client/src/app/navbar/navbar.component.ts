import { Component, OnInit } from "@angular/core";
import { AuthenticationService } from "@app/services/authentication.service";
import { User } from "@app/models/user";
import { Router } from "@angular/router";

@Component({
  selector: "app-navbar",
  templateUrl: "./navbar.component.html",
  styleUrls: ["./navbar.component.css"]
})
export class NavbarComponent implements OnInit {
  currentUser: User;

  constructor(
    private authService: AuthenticationService,
    private router: Router
  ) {
    this.authService.getCurrentUserSubject.subscribe(currUser => {
      this.currentUser = currUser;
    });
  }

  ngOnInit(): void {
    this.currentUser = this.authService.currentUserValue;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(["/login"]);
    this.currentUser = this.authService.currentUserValue;
  }
}
