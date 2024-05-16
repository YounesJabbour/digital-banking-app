import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  formLogin!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.formLogin = this.fb.group({
      email: this.fb.control(''),
      password: this.fb.control(''),
    });
  }

  handleLogin() {
    // let email = this.formLogin.get('username')?.value;
    // let password = this.formLogin.get('password')?.value;

    const newCustomer = this.formLogin.value;
    this.authService.login(newCustomer).subscribe({
      next: (data) => {
        this.authService.loadProfile(data);
        console.log('Login successful');
        this.router.navigate(['admin/customers']);
      },
      error: (error) => {
        console.log(error);
      },
    });
  }

  navigateToRegister() {
    this.router.navigate(['/register']);
  }

  navigateToChangePassword() {
    this.router.navigate(['/change-password']);
  }
}
