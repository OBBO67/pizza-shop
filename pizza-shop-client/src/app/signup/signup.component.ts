import { Component, OnInit } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
// import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthenticationService } from "@app/services/authentication.service";
import { UserService } from "@app/services/user.service";
import { first } from "rxjs/operators";
import { AlertService } from "@app/services/alert.service";

@Component({
  selector: "app-signup",
  templateUrl: "./signup.component.html",
  styleUrls: ["./signup.component.css"]
})
export class SignupComponent implements OnInit {
  signupForm: FormGroup;
  submitted = false;

  // TODO: Add alertService
  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authenticationService: AuthenticationService,
    private userService: UserService,
    private alertService: AlertService
  ) {
    // redirect to menu if user already logged in
    if (this.authenticationService.currentUserValue) {
      this.router.navigate(["/"]);
    }
  }

  ngOnInit(): void {
    // groups together each FormControl to create a FormGroup
    this.signupForm = this.formBuilder.group({
      firstName: ["", Validators.required],
      lastName: ["", Validators.required],
      username: ["", Validators.required],
      houseNumber: ["", Validators.required],
      addressLine1: ["", Validators.required],
      addressLine2: ["", Validators.required],
      city: ["", Validators.required],
      postcode: ["", Validators.required],
      password: ["", [Validators.required, Validators.minLength(6)]]
    });
  }

  get firstNameGroup(): FormGroup {
    return this.signupForm.controls.firstName as FormGroup;
  }

  get lastNameGroup(): FormGroup {
    return this.signupForm.controls.lastName as FormGroup;
  }

  get usernameGroup(): FormGroup {
    return this.signupForm.controls.username as FormGroup;
  }

  get houseNumberGroup(): FormGroup {
    return this.signupForm.controls.houseNumber as FormGroup;
  }

  get addressLine1Group(): FormGroup {
    return this.signupForm.controls.addressLine1 as FormGroup;
  }

  get addressLine2Group(): FormGroup {
    return this.signupForm.controls.addressLine2 as FormGroup;
  }

  get cityGroup(): FormGroup {
    return this.signupForm.controls.city as FormGroup;
  }

  get postcodeGroup(): FormGroup {
    return this.signupForm.controls.postcode as FormGroup;
  }

  get passwordGroup(): FormGroup {
    return this.signupForm.controls.password as FormGroup;
  }

  // TODO: Handle error
  onSubmit() {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.signupForm.invalid) {
      console.log("Form is invlaid -> returning");
      console.log(`Errors found: ${this.signupForm.errors}`);
      return;
    }

    const signUpData = this.signupForm.value;
    console.log(`Signup data from form: ${JSON.stringify(signUpData)}`);

    // this.loading = true;
    this.userService
      .signup(this.signupForm.value)
      .pipe(first())
      .subscribe(data => {
        console.log(`Returned data: ${JSON.stringify(data)}`);
        this.alertService.success("Registration successful", true);
        this.router.navigate(["/login"]);
      });
  }
}
