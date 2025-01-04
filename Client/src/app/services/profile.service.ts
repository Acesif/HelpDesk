import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import {AuthService} from './auth.service';
import {ipConfigService} from '../shared/ip-config.service';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  private userApiUrl: string;

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private ipConfigService: ipConfigService,
  ) {
    this.userApiUrl = `${this.ipConfigService.getAddress()}/api/user`;
  }

  getUserInfo(userId: number): any {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`,
    });
    return this.http.get(`${this.userApiUrl}/${userId}`, {headers});
  }
}
