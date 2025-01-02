import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {IpconfigService} from '../shared/ipconfig.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  apiUrl: string;

  constructor(
    private http: HttpClient,
    private ipconfigService: IpconfigService,
  ) {
    this.apiUrl = `${this.ipconfigService.getAddress()}/api/user`;
  }

  login(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, userData);
  }
  refreshToken(oldToken: string) {
    return this.http.get(`${this.apiUrl}/refresh/${oldToken}`);
  }
}
