import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import {ipConfigService} from '../shared/ip-config.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  apiUrl: string;

  constructor(
    private http: HttpClient,
    private ipConfigService: ipConfigService,
  ) {
    this.apiUrl = `${this.ipConfigService.getAddress()}/api/user`;
  }

  login(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, userData);
  }
  refreshToken(oldToken: string) {
    return this.http.get(`${this.apiUrl}/refresh/${oldToken}`);
  }

  referredLoginFromGRS(userData: any): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post(`${this.apiUrl}/referrer-grs`, userData, { headers });
  }
}
