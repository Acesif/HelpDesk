import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  // private userApiUrl = 'http://localhost:7890/api/user';
  private userApiUrl = 'http://localhost:7890/helpdesk/api/user';

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {}

  getUserInfo(userId: number): any {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`,
    });
    return this.http.get(`${this.userApiUrl}/${userId}`, {headers});
  }
}
