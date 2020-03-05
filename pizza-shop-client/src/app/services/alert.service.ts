import { Injectable } from "@angular/core";
import { Subject, Observable } from "rxjs";
import { Router, NavigationStart } from "@angular/router";

@Injectable({
  providedIn: "root"
})
export class AlertService {
  private subject = new Subject<any>();
  private keep = false;

  constructor(private router: Router) {
    // clear the alert message on route change
    // unless the keep flag is true
    this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        if (this.keep) {
          // only keep for single route change
          this.keep = false;
        } else {
          // clear alert message
          this.clear();
        }
      }
    });
  }

  getAlert(): Observable<any> {
    return this.subject.asObservable();
  }

  success(message: string, keepAfterRouteChange = false) {
    this.keep = keepAfterRouteChange;
    this.subject.next({ type: "success", text: message });
  }

  error(message: string, keepAfterRouteChange = false) {
    this.keep = keepAfterRouteChange;
    this.subject.next({ type: "error", text: message });
  }

  clear() {
    // clear by calling subject.next() without parameters
    this.subject.next();
  }
}
