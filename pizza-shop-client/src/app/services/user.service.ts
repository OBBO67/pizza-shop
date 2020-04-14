import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { User } from "@app/models/user";

/*
 * Contains a standard set of CRUD methods for managing users.
 * Acts as an interface between the Angular app and the backend.
 */

@Injectable({
  providedIn: "root"
})
export class UserService {
  constructor(private http: HttpClient) {}

  /**
   * CREATE - Sign a new user up
   * @param user - the user to sign up
   */
  signup(user: User) {
    console.log(`User signup data: ${JSON.stringify(user)}`);
    return this.http.post("http://localhost:8080/api/signup", user);
  }

  /**
   * Checks to see if a username is available or if it is
   * already in use.
   * @param username - the username to check it's availability
   */
  checkUsername(username: string) {
    console.log(`Checking username: ${username}`);
    return this.http.get(
      `http://localhost:8080/api/user/username?username=${username}`
    );
  }
}
