import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer } from '../models/customer.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  updateCustomer(updatedCustomer: Customer): Observable<Customer> {
    return this.http.put<Customer>(
      `${this.baseUrl}/customers`,
      updatedCustomer
    );
  }
  constructor(private http: HttpClient) {}

  baseUrl: string = environment.baseUrl;

  public getCustomers(): Observable<Array<Customer>> {
    return this.http.get<Array<Customer>>(`${this.baseUrl}/customers`);
  }

  public getCustomer(id: number): Observable<Customer> {
    return this.http.get<Customer>(`${this.baseUrl}/customers/${id}`);
  }

  public searchCustomers(keyword: string): Observable<Array<Customer>> {
    return this.http.get<Array<Customer>>(
      `${this.baseUrl}/customers/searchCustomer?keyword=${keyword}`
    );
  }

  public addCustomer(customer: Customer): Observable<Customer> {
    return this.http.post<Customer>(`${this.baseUrl}/customers`, customer);
  }

  deleteCustomer(id: number | undefined) {
    return this.http.delete(`${this.baseUrl}/customers/${id}`);
  }
}
