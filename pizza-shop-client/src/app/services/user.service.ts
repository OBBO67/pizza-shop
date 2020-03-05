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

  signup(user: User) {
    return this.http.post("api/users/signup", user);
  }
}
