import { Component, Renderer2 } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Customer } from '../models/customer.model';
import { CustomerService } from '../services/customer.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-new-customer',
  templateUrl: './new-customer.component.html',
  styleUrl: './new-customer.component.css'
})
export class NewCustomerComponent {

  constructor(private customerService:CustomerService, private fb: FormBuilder, private render:Renderer2) { }

  newCustomerFormGroup : FormGroup | undefined;
  errorMessage : string = '';
  newCustomerSuccessMessage : string = '';

  ngOnInit() {
    this.newCustomerFormGroup = this.fb.group({
        name: this.fb.control('',[Validators.required, Validators.minLength(3)]),
        email: this.fb.control('',[Validators.required, Validators.email])
    });
  }

  handleNewCustomerSubmit() {
    const customer: Customer = this.newCustomerFormGroup?.value;

    this.customerService.addCustomer(customer).subscribe({
      next: (response) => {
        this.newCustomerFormGroup?.reset();
        this.newCustomerSuccessMessage = 'Customer added successfully';
        setTimeout(() => {
          this.newCustomerSuccessMessage = '';
        }, 2000);
      },
      error: (error) => {
        this.errorMessage = error.message;
      }
    })
  }

}
