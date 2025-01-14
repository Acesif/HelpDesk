import { Component } from '@angular/core';
import {AuthService} from '../services/auth.service';
import {InterceptorService} from '../services/interceptor.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  appName = "GRS Helpdesk"

  constructor(
    private auth: AuthService,
    private toast: ToastrService
  ) {}

  isAdmin(): boolean {
    const designation = this.auth.getUserDesignation();
    return designation === 'GRO' || designation === 'VENDOR';
  }

  isSuperAdmin() {
    const designation = this.auth.getUserDesignation();
    return designation === 'SUPERADMIN';
  }

  logout() {
    this.auth.logout();
    this.toast.success('Logout successful!', 'Success');
  }

  isAuthenticated() {
    return this.auth.isAuthenticated();
  }
}
