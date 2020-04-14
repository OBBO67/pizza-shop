import { Component, OnInit } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
// import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthenticationService } from "@app/services/authentication.service";
import { UserService } from "@app/services/user.service";
import { first } from "rxjs/operators";
import { AlertService } from "@app/services/alert.service";
import { Address } from "@app/models/customer-address";
import { User } from "@app/models/user";

@Component({
  selector: "app-signup",
  templateUrl: "./signup.component.html",
  styleUrls: ["./signup.component.css"]
})
export class SignupComponent implements OnInit {
  signupForm: FormGroup;
  submitted = false;

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
      username: [
        "",
        [Validators.required, Validators.minLength(3), Validators.maxLength(20)]
      ],
      email: ["", [Validators.required, Validators.email]],
      houseNumber: ["", Validators.required],
      addressLine1: ["", Validators.required],
      addressLine2: ["", Validators.required],
      city: ["", Validators.required],
      postcode: ["", Validators.required], // TODO: uk postcode validator
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

  get emailGroup(): FormGroup {
    return this.signupForm.controls.email as FormGroup;
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

  onSubmit() {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.signupForm.invalid) {
      console.log("Form is invlaid -> returning");
      return;
    }

    const signUpData = this.signupForm.value;
    console.log(`Signup data from form: ${JSON.stringify(signUpData)}`);

    const newUser = this.buildUser(signUpData);

    console.log(`New user from form: ${JSON.stringify(newUser)}`);

    // this.loading = true;
    this.userService
      .signup(newUser)
      .pipe(first())
      .subscribe(data => {
        console.log(`Returned data: ${JSON.stringify(data)}`);
        this.alertService.success("Registration successful", true);
        this.router.navigate(["/login"]);
      });
  }

  private buildUser(signUpData: any): User {
    const userAddress = new Address();
    userAddress.houseNumber = signUpData.houseNumber;
    userAddress.addressLine1 = signUpData.addressLine1;
    userAddress.addressLine2 = signUpData.addressLine2;
    userAddress.city = signUpData.city;
    userAddress.postcode = signUpData.postcode;

    const newUser = new User();
    newUser.firstName = signUpData.firstName;
    newUser.lastName = signUpData.lastName;
    newUser.username = signUpData.username;
    newUser.email = signUpData.email;
    newUser.addresses.push(userAddress);
    newUser.password = signUpData.password;

    return newUser;
  }
}
