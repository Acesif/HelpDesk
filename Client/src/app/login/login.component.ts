import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginService } from '../services/login.service';
import { Router } from '@angular/router';
import {InterceptorService} from '../services/interceptor.service';
import {AuthService} from '../services/auth.service'; // Optional: For navigation after login

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private loginService: LoginService,
    private interceptor: InterceptorService,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit() {
    this.interceptor.validateRoutePermission();
  }

  onLogin(): void {
    if (this.loginForm.valid) {
      this.errorMessage = null;
      const loginData = this.loginForm.value;

      this.loginService.login(loginData).subscribe({
        next: (response) => {
          console.log('Login successful:', response);
          this.authService.saveToken(response.data.token);
          this.interceptor.validateRoutePermission();
        },
        error: (error) => {
          console.error('Login failed:', error);
          this.errorMessage = error.error.message || 'An error occurred. Please try again.';
        }
      });
    } else {
      console.log('Login form is invalid');
    }
  }

  navigateToRegister(): void {
    this.router.navigate(['/auth/register']);
  }
}
