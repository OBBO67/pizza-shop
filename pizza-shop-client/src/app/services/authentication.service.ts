import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { BehaviorSubject, Observable } from "rxjs";
import { map } from "rxjs/operators";

import { User } from "@app/models/user";

/*
 * Used to login and logout of the app. Notifies other components when the user logs in and out.
 * Also allows components to get the currently logged in user.
 */

@Injectable({
  providedIn: "root"
})
export class AuthenticationService {
  private currentUserSubject: BehaviorSubject<User>;
  private currentUser: Observable<User>;

  constructor(private http: HttpClient) {
    /**
     * A BehaviourSubject keeps hold of the current user and emits it
     * to any new subscribers as soon as they subscribe. */

    this.currentUserSubject = new BehaviorSubject<User>(
      JSON.parse(localStorage.getItem("currentUser"))
    );
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User {
    return this.currentUserSubject.value;
  }

  public get getCurrentUserSubject(): BehaviorSubject<User> {
    return this.currentUserSubject;
  }

  /*
   * Sends the users credentials to the api login route.
   * If successful the user object including the JWT are stored in localStorage.
   * This keeps the user logged in between page refreshes.
   * The user is then published to all subscribers with the call to this.currentUserSubject.next(user).
   */
  login(username: string, password: string) {
    return this.http
      .post<any>("http://localhost:8080/api/login", { username, password })
      .pipe(
        map(user => {
          // store user details and jwt token in local storage
          // to keep user logged in between page refreshes
          // use switchMap to switch to a new observable i.e. a new request

          localStorage.setItem("currentUser", JSON.stringify(user));
          console.log(JSON.stringify(user));
          this.currentUserSubject.next(user);
          return user;
        })
      );
  }

  /*
   * Removes the current user from local storage and publishes null to the
   * currentUserSubject to notify all subscribers that the user has logged out.
   */
  logout() {
    // remove user from local storage to log user out
    localStorage.removeItem("currentUser");
    this.currentUserSubject.next(null);
  }
}
