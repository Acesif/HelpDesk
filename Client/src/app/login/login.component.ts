import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginService } from '../services/login.service';
import { Router } from '@angular/router';
import {InterceptorService} from '../services/interceptor.service';
import {AuthService} from '../services/auth.service';
import {ToastrService} from 'ngx-toastr';

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
    private router: Router,
    private toast: ToastrService,
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
          this.toast.success('Login successful!', 'Success');
          this.authService.saveToken(response.data.token);
          this.interceptor.validateRoutePermission();
        },
        error: (error) => {
          this.toast.error('Wrong credentials', 'Login Failed');
          console.error(error.message);
          this.loginForm.reset();
        }
      });
    } else {
      console.log('Login form is invalid');
    }
  }

  navigateToRegister(): void {
    this.router.navigate(['/auth/register']);
  }

  onForget() {
    this.toast.error("Forgot Password");
  }
}
