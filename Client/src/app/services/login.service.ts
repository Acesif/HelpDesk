import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  // private apiUrl = 'http://94.250.203.197:7890/api/user/login';
  private apiUrl = 'http://94.250.203.197:7890/helpdesk/api/user/login';

  constructor(private http: HttpClient) {}

  login(userData: any): Observable<any> {
    return this.http.post(this.apiUrl, userData);
  }
}
