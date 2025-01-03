import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import {AuthService} from './auth.service';
import {IpconfigService} from '../shared/ipconfig.service';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  private userApiUrl: string;

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private ipconfigService: IpconfigService,
  ) {
    this.userApiUrl = `${this.ipconfigService.getAddress()}/api/user`;
  }

  getUserInfo(userId: number): any {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`,
    });
    return this.http.get(`${this.userApiUrl}/${userId}`, {headers});
  }
}
