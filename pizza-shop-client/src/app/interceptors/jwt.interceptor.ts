import { Injectable } from "@angular/core";
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from "@angular/common/http";
import { Observable } from "rxjs";

import { AuthenticationService } from "@app/services/authentication.service";

/*
 * Intercepts http requests from the app to add a JWT
 * to the Authorization header if the user is logged in.
 */

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private authenticationService: AuthenticationService) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    let currentUser = this.authenticationService.currentUserValue;

    if (currentUser && currentUser.token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${currentUser.token}`
        }
      });
    }

    // pass request onto the next interceptor
    return next.handle(request);
  }
}
