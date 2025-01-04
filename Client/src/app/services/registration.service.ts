import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {ipConfigService} from '../shared/ip-config.service';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {
  private apiUrl: string;

  constructor(
    private http: HttpClient,
    private ipConfigService: ipConfigService,
  ) {
    this.apiUrl = `${this.ipConfigService.getAddress()}/api/user/create`
  }

  register(userData: any): Observable<any> {
    return this.http.post(this.apiUrl, userData);
  }
}
