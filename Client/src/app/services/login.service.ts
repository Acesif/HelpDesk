import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private apiUrl = 'http://localhost:7890/api/user/login';

  constructor(private http: HttpClient) {}

  login(userData: any): Observable<any> {
    return this.http.post(this.apiUrl, userData);
  }
}