import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../services/customer.service';
import { Customer } from '../models/customer.model';

@Component({
  selector: 'app-edit-customer',
  templateUrl: './edit-customer.component.html',
  styleUrls: ['./edit-customer.component.css'],
})
export class EditCustomerComponent implements OnInit {
  editCustomerFormGroup!: FormGroup;
  editCustomerSuccessMessage!: string;
  errorMessage!: string;
  customer!: Customer;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private customerService: CustomerService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const customerId: any = this.route.snapshot.paramMap.get('id');
    if (customerId) {
      this.customerService.getCustomer(customerId).subscribe({
        next: (data) => {
          this.customer = data;
          this.initializeForm();
        },
        error: (error) => {
          console.error(error);
          this.errorMessage = 'Failed to load customer data';
        },
      });
    }
  }

  initializeForm(): void {
    this.editCustomerFormGroup = this.fb.group({
      id: [{ value: this.customer.id, disabled: true }],
      name: [this.customer.name, Validators.required],
      email: [this.customer.email, [Validators.required, Validators.email]],
    });
  }

  handleEditCustomerSubmit(): void {
    if (this.editCustomerFormGroup.valid) {
      const updatedCustomer = {
        ...this.editCustomerFormGroup.getRawValue(),
        id: this.customer.id,
      };

      this.customerService.updateCustomer(updatedCustomer).subscribe({
        // update customer
        next: () => {
          this.editCustomerSuccessMessage = 'Customer updated successfully!';
        },
        error: (error) => {
          console.error(error);
          this.errorMessage = 'Failed to update customer';
        },
      });
    } else {
      this.errorMessage = 'Please fix the errors in the form.';
    }
  }
}
