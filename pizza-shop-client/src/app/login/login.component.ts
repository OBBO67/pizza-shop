import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";

import { AuthenticationService } from "@app/services/authentication.service";
import { first } from "rxjs/operators";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.css"]
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  submitted = false;
  returnUrl: string;
  error = "";

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService
  ) {
    // redirect to menu if already logged in
    if (this.authenticationService.currentUserValue) {
      this.router.navigate(["/menu"]);
    }
  }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: ["", Validators.required],
      password: ["", Validators.required]
    });

    // get return url from route params or default to "/"
    this.returnUrl = this.route.snapshot.queryParams["returnUrl"] || "/";
  }

  get usernameGroup(): FormGroup {
    return this.loginForm.controls.username as FormGroup;
  }

  get passwordGroup(): FormGroup {
    return this.loginForm.controls.password as FormGroup;
  }

  onSubmit() {
    this.submitted = true;

    // return if form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    this.authenticationService
      .login(
        this.loginForm.controls.username.value,
        this.loginForm.controls.password.value
      )
      .pipe(first())
      .subscribe(
        data => {
          console.log(`Data returned from login: ${data}`); // this is just the user here
          console.log(this.returnUrl);
          this.router.navigate([this.returnUrl]);
        },
        error => {
          this.error = error;
        }
      );
  }
}
