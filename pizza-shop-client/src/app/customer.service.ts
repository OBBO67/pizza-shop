import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";

import { Observable, of } from "rxjs";
import { catchError, map, tap } from "rxjs/operators";

import { MessageService } from "./message.service";
import { Customer } from "./customer";

@Injectable({
  providedIn: "root"
})
export class CustomerService {
  private baseUrl = "http://localhost:8080/api/customers";
  httpOptions = {
    headers: new HttpHeaders({
      "Content-Type": "application/json",
      "Access-Control-Allow-Credentials": "true"
    })
  };

  constructor(
    private http: HttpClient,
    private messageService: MessageService
  ) {}

  /** GET customer by id. Will 404 if id not found */
  getCustomerById(id: number): Observable<Customer> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.get<Customer>(url).pipe(
      tap(_ => this.log(`fetched customer id=${id}`)),
      catchError(this.handleError<Customer>(`getCustomerById id=${id}`))
    );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = "operation", result?: T) {
    return (error: any): Observable<T> => {
      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a HeroService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`HeroService: ${message}`);
  }
}
