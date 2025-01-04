import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {AuthService} from './auth.service';
import {Router} from '@angular/router';
import {ipConfigService} from '../shared/ip-config.service';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  dashboardApiUrl: string;

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private ipConfigService: ipConfigService,
  ) {
    this.dashboardApiUrl = `${this.ipConfigService.getAddress()}/api/dashboard`;
  }

  getDashboardData(): any {

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`,
    });

    return this.http.get<any>(`${this.dashboardApiUrl}/count`, {headers});
  }
}
