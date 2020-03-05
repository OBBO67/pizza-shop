import { Component, OnInit } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthenticationService } from "@app/services/authentication.service";
import { UserService } from "@app/services/user.service";
import { first } from "rxjs/operators";

@Component({
  selector: "app-signup",
  templateUrl: "./signup.component.html",
  styleUrls: ["./signup.component.css"]
})
export class SignupComponent implements OnInit {
  private signupForm: FormGroup;
  private submitted = false;

  // TODO: Add alertService
  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authenticationService: AuthenticationService,
    private userService: UserService
  ) {
    // redirect to menu if user already logged in
    if (this.authenticationService.currentUserValue) {
      this.router.navigate(["/"]);
    }
  }

  ngOnInit(): void {
    this.signupForm = this.formBuilder.group({
      firstName: ["", Validators.required],
      lastName: ["", Validators.required],
      username: ["", Validators.required],
      password: ["", [Validators.required, Validators.minLength(6)]]
    });
  }

  // TODO: Handle error
  onSubmit() {
    this.submitted = true;

    // reset alerts on submit
    // this.alertService.clear();

    // stop here if form is invalid
    if (this.signupForm.invalid) {
      return;
    }

    // this.loading = true;
    this.userService
      .signup(this.signupForm.value)
      .pipe(first())
      .subscribe(data => {
        // this.alertService.success("Registration successful", true);
        this.router.navigate(["/login"]);
      });
  }
}
