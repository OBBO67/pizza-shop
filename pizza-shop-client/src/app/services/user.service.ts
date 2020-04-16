import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { User } from "@app/models/user";
import { Observable, of } from "rxjs";
import { first, delay } from "rxjs/operators";

/*
 * Contains a standard set of CRUD methods for managing users.
 * Acts as an interface between the Angular app and the backend.
 */

@Injectable({
  providedIn: "root",
})
export class UserService {
  usernameTaken = false;

  constructor(private http: HttpClient) {}

  /**
   * CREATE - Sign a new user up
   * @param user - the user to sign up
   */
  signup(user: User): Observable<any> {
    console.log(`User signup data: ${JSON.stringify(user)}`);
    return this.http.post("http://localhost:8080/api/signup", user);
  }

  /**
   * Checks to see if a username is available or if it is
   * already in use.
   * @param username - the username to check it's availability
   */
  checkUsername(username: string): Observable<any> {
    console.log(`Checking username: ${username}`);
    return this.http.get(
      `http://localhost:8080/api/user/username?username=${username}`
    );
    // .pipe()
    // .subscribe((data) => {
    //   console.log(`Data from check username: ${data}`);
    //   if (data !== null) {
    //     console.log("Username taken is getting set to TRUE");
    //     this.usernameTaken = true;
    //     console.log(`usernameTaken value: ${this.usernameTaken}`);
    //   } else {
    //     this.usernameTaken = false;
    //   }
    // });

    // console.log("Going to second return statement");
    // console.log(`usernameTaken value: ${this.usernameTaken}`);
    // return of(this.usernameTaken).pipe(delay(400));
  }
}
