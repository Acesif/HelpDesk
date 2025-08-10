import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import {ipConfigService} from '../shared/ip-config.service';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  apiUrl: string;

  constructor(
    private http: HttpClient,
    private ipConfigService: ipConfigService,
    private authService: AuthService
  ) {
    this.apiUrl = `${this.ipConfigService.getAddress()}/api/user`;
  }

  login(userData: any): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': 'https://desktop-34m6a2h.tailcfa47b.ts.net',
      'Access-Control-Allow-Credentials': 'true',
      'Access-Control-Allow-Headers': 'Authorization',
    });
    return this.http.post(`${this.apiUrl}/login`, userData, { headers });
  }
  refreshToken(oldToken: string) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': 'https://desktop-34m6a2h.tailcfa47b.ts.net',
      'Access-Control-Allow-Credentials': 'true',
    });
    return this.http.get(`${this.apiUrl}/refresh/${oldToken}`, { headers });
  }
}
