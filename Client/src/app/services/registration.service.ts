import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {
  // private apiUrl = 'http://localhost:7890/api/user/create';
  private apiUrl = 'http://localhost:7890/helpdesk/api/user/create';

  constructor(private http: HttpClient) {}

  register(userData: any): Observable<any> {
    return this.http.post(this.apiUrl, userData);
  }
}
