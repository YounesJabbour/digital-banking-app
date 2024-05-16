import { Component } from '@angular/core';
import { CustomerService } from '../services/customer.service';
import { Customer } from '../models/customer.model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css',
})
export class CustomersComponent {
  handleEditCustomer(c: Customer) {
    this.router.navigate(['/admin/edit-customer', c.id], {
      state: { customer: c },
    });
  }

  customers: Array<Customer> | undefined;
  errorMessage: Object = '';
  searchFromGroup: FormGroup | undefined;

  constructor(
    private customerService: CustomerService,
    private fb: FormBuilder,
    private router: Router,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.searchFromGroup = this.fb.group({
      keyword: this.fb.control(''),
    });

    this.handleSearchCustomers();
  }

  handleSearchCustomers() {
    this.customerService
      .searchCustomers(this.searchFromGroup?.value.keyword)
      .subscribe({
        next: (data) => {
          this.customers = data;
        },
        error: (error) => (this.errorMessage = error.message),
      });
  }

  handleDeleteCustomer(customer: Customer) {
    let c = confirm('Are you sure you want to delete this customer?');
    if (!c) {
      return;
    }
    this.customerService.deleteCustomer(customer.id).subscribe({
      next: (data) => {
        this.handleSearchCustomers();
      },
      error: (error) => (this.errorMessage = error.message),
    });
  }
}
