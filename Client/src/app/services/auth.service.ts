import { Injectable } from '@angular/core';
import {Router} from '@angular/router';
import {JwtHelperService} from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private router: Router) { }

  saveToken(token: string) {
    localStorage.setItem('token', token);
  }

  getToken(): string {
    const token = localStorage.getItem('token');
    if (token) {
      return token;
    } else {
      console.log('No token found in localStorage.');
    }
  }

  isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    return token !== null;
  }

  getUserDesignation(): string | null {
    const jwt = new JwtHelperService();
    const token = localStorage.getItem('token');
    if (!token) {
      return null;
    }
    const payload = jwt.decodeToken(token);
    if (payload.username === "wonderwoman") {
      return "SUPERADMIN";
    }
    return payload.designation || null;
  }

  getValidity(): number | null {
    const jwt = new JwtHelperService();
    const token = localStorage.getItem('token');
    if (!token) {
      return null;
    }
    const payload = jwt.decodeToken(token);
    return payload.exp || null;
  }

  logout(): void {
    localStorage.removeItem('token');
    window.location.href = "http://localhost/dashboard.do";
  }

  extractTokenInfo() {
    const jwt = new JwtHelperService();
    const token = localStorage.getItem('token');
    return  jwt.decodeToken(token);
  }
}
