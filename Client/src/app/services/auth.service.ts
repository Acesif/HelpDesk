import { Injectable } from '@angular/core';
import {Router} from '@angular/router';

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
    return !!token;
  }

  getUserDesignation(): string | null {
    const token = localStorage.getItem('token');
    if (!token) {
      return null;
    }
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.designation || null;
  }

  getValidity(): number | null {
    const token = localStorage.getItem('token');
    if (!token) {
      return null;
    }
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.exp || null;
  }

  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['auth','login']);
  }
}
