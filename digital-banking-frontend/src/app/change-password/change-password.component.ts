import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.css',
})
export class ChangePasswordComponent {
  changePasswordForm: FormGroup;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.changePasswordForm = this.fb.group(
      {
        email: ['', [Validators.required, Validators.email]],
        currentPassword: ['', [Validators.required]],
        newPassword: ['', [Validators.required, Validators.minLength(8)]],
        confirmPassword: ['', [Validators.required]],
      },
      { validator: this.passwordMatchValidator }
    );
  }

  passwordMatchValidator(form: FormGroup) {
    const newPassword = form.get('newPassword')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return newPassword === confirmPassword ? null : { mismatch: true };
  }

  handleChangePassword() {
    if (this.changePasswordForm.valid) {
      const { email, currentPassword, newPassword } =
        this.changePasswordForm.value;
      this.authService
        .changePassword(email, currentPassword, newPassword)
        .subscribe({
          next: () => {
            this.successMessage = 'Password changed successfully';
            this.errorMessage = null;
            this.changePasswordForm.reset();
            this.router.navigate(['/login']);
          },
          error: (err) => {
            this.successMessage = null;
            this.errorMessage = err.message;
          },
        });
    }
  }
}
