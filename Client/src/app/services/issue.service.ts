import {Issue} from '../../model/Issue.model';
import {EventEmitter, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {AuthService} from './auth.service';
import {map, Observable} from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class IssueService {
  statusChanged: EventEmitter<boolean> = new EventEmitter();
  issueChanged = new EventEmitter<Issue[]>();

  private issues: Issue[] = [];

  private apiUrl = 'http://localhost:7890/api/issue';

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {}

  // createIssue(issue: Issue): Observable<any> {
  createIssue(issue: Issue): any {
    const issueData = new FormData();

    issueData.append('title', issue.title);
    issueData.append('description', issue.description);
    issueData.append('category', issue.issueCategory);
    issueData.append('officeId', issue.office.toString());

    if (issue.attachments && issue.attachments.length > 0) {
      issue.attachments.forEach((file) => {
        issueData.append('attachments', file, file.name);
      });
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`,
    });

    return this.http.post(`${this.apiUrl}/new`, issueData, {headers}).subscribe((res) => console.log(res));
  }

  getIssues(): Observable<Issue[]> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`,
    });

    return this.http.get<any>(`${this.apiUrl}/user`, { headers }).pipe(
      map((res) => {
        if (res.status === 'OK') {
          return res.data.map(
            (issue: any) =>
              new Issue(
                issue.trackingNumber,
                issue.title,
                issue.description,
                issue.category,
                issue.status,
                issue.officeId
              )
          );
        } else {
          console.error('Failed to retrieve issues:', res.message);
          return [];
        }
      })
    );
  }

  getIssueDetails(issueId: string): any {
    return this.http.get<any>(`${this.apiUrl}/${issueId}`);
  }
}
