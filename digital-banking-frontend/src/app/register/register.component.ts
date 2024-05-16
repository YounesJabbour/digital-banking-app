import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  registerCustomerFormGroup!: FormGroup;
  registerCustomerSuccessMessage!: string;
  errorMessage!: string;

  constructor(private fb: FormBuilder, private authService: AuthService) {}

  ngOnInit(): void {
    this.initializeForm();
  }

  initializeForm(): void {
    this.registerCustomerFormGroup = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  handleRegisterCustomerSubmit(): void {
    if (this.registerCustomerFormGroup.valid) {
      const newCustomer = this.registerCustomerFormGroup.value;
      console.log(newCustomer);
      this.authService.register(newCustomer).subscribe({
        next: () => {
          this.registerCustomerSuccessMessage =
            'Customer registered successfully!';
        },
        error: (error) => {
          console.error(error);
          this.errorMessage = 'Failed to register customer';
        },
      });
    } else {
      this.errorMessage = 'Please fix the errors in the form.';
    }
  }
}
