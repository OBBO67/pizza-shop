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
   */
  signup(user: User) {
    console.log(`User signup data: ${JSON.stringify(user)}`);
    return this.http.post("http://localhost:8080/api/signup", user);
  }
}
