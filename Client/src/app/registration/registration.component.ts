import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RegistrationService } from '../services/registration.service';
import {Router} from '@angular/router';
import {AuthService} from '../services/auth.service';
import {InterceptorService} from '../services/interceptor.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.scss'
})
export class RegistrationComponent {
  registrationForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private registrationService: RegistrationService,
    private router: Router,
    private tokenService: AuthService,
    private interceptor: InterceptorService,
    private toast: ToastrService
  ) { }

  ngOnInit(): void {
    this.interceptor.validateRoutePermission();

    this.registrationForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^\d{11}$/)]],
      officeId: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      designation: ['', Validators.required],
    });
  }

  onSubmit(): void {
    if (this.registrationForm.valid) {
      console.log(this.registrationForm.value);
      this.registrationService.register(this.registrationForm.value).subscribe({
        next: (response: any) => {
          if (response.status === 'CREATED') {
            this.tokenService.saveToken(response.data.token);
            this.registrationForm.reset();
            this.router.navigate(['admin','dashboard'])
              .then(() => {
                this.toast.success('Registration successful!', 'Success');
              })
              .catch((err) => {
                console.log(err.message);
                this.toast.error('Registration Failed!', 'Success');
              });
          }
        },
        error: (err: any) => console.error('Error registering', err)
      });
    } else {
        alert("Please fill all the fields");
    }
  }

  navigateToLogin(): void {
    this.router.navigate(['auth','login']);
  }
}
