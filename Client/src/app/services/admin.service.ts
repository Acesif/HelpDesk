import { Injectable } from '@angular/core';
import {map, Observable} from 'rxjs';
import {Issue} from '../../model/Issue.model';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {AuthService} from './auth.service';
import {ipConfigService} from '../shared/ip-config.service';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private apiUrl: string;

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private ipConfigService: ipConfigService
  ) {
    this.apiUrl = `${this.ipConfigService.getAddress()}/api/issue`;
  }

  getInboxIssues(page: number): Observable<Issue[]> {
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
                issue.updatedOn,
                null
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
