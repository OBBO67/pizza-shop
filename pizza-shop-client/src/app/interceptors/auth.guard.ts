import { Injectable } from "@angular/core";
import {
  Router,
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot
} from "@angular/router";

// import { AuthenticationService } from '@app/_services';

@Injectable({ providedIn: "root" })
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router // private authService: AuthenticationService
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    // const currentUser = this.authService.currentUserValue;

    // check if the current user is logged in
    if (false) {
      return true;
    }

    // if here then the user is not logged in
    // redirect to the login page
    this.router.navigate(["/login"], { queryParams: { returnUrl: state.url } });
    return false;
  }
}
