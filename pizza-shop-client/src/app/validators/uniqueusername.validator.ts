import { Injectable, Directive, forwardRef } from "@angular/core";
import {
  AsyncValidator,
  AbstractControl,
  ValidationErrors,
  NG_ASYNC_VALIDATORS,
} from "@angular/forms";
import { UserService } from "@app/services/user.service";
import { Observable, of } from "rxjs";
import { map, catchError, delay, tap, first, last } from "rxjs/operators";

@Injectable({
  providedIn: "root",
})
export class UniqueUsernameValidator implements AsyncValidator {
  constructor(private userService: UserService) {}

  /**
   * Calls the UserService's checkUsername method to send a request to the
   * backend to check if the username is already in use. If it is already taken,
   * then adds usernameTaken to the ValidationErrors map.
   * @param control - the form control object
   */
  validate(control: AbstractControl): Observable<ValidationErrors | null> {
    return this.userService.checkUsername(control.value).pipe(
      map((user) => {
        console.log(`Is username taken? ${user}`);
        if (user) {
          console.log("Username is already taken");
          return { usernameTaken: true };
        } else {
          return null;
        }
      }),
      catchError(() => of(null))
    );
  }
}
