import { Injectable } from '@angular/core';
import {map, Observable} from 'rxjs';
import {Issue} from '../../model/Issue.model';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {AuthService} from './auth.service';
import {IpconfigService} from '../shared/ipconfig.service';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private apiUrl: string;

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private ipconfigService: IpconfigService
  ) {
    this.apiUrl = `${this.ipconfigService.getAddress()}/api/issue`;
  }

  getInboxIssues(): Observable<Issue[]> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`,
    });

    return this.http.get<any>(`${this.apiUrl}/inbox`, { headers }).pipe(
      map((res) => {
        if (res.status === 'OK') {
          return res.data.map(
            (issue: any) =>
              new Issue(
                issue.id,
                issue.trackingNumber,
                issue.title,
                issue.description,
                issue.category,
                issue.status,
                issue.officeId,
                issue.postedOn,
                issue.postedBy,
                issue.updatedOn
              )
          );
        } else {
          console.error('Failed to retrieve issues:', res.message);
          return [];
        }
      })
    );
  }
}
