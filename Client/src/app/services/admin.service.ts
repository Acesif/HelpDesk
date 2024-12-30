import { Injectable } from '@angular/core';
import {map, Observable} from 'rxjs';
import {Issue} from '../../model/Issue.model';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) { }

  private apiUrl = 'http://localhost:7890/api/issue'

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
