import {Issue} from '../../model/Issue.model';
import {EventEmitter, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {AuthService} from './auth.service';
import {map, Observable} from 'rxjs';
import {ipConfigService} from '../shared/ip-config.service';


@Injectable({
  providedIn: 'root',
})
export class IssueService {
  statusChanged: EventEmitter<boolean> = new EventEmitter();

  issueApiUrl: string;
  issueReplyApiUrl: string;

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private ipConfigService: ipConfigService,
  ) {
    this.issueApiUrl = `${this.ipConfigService.getAddress()}/api/issue`;
    this.issueReplyApiUrl = `${this.ipConfigService.getAddress()}/api/issue_reply`;
  }

  createIssue(issue: Issue): any {
    const issueData = new FormData();

    issueData.append('title', issue.title);
    issueData.append('description', issue.description);
    issueData.append('category', issue.category);
    issueData.append('officeId', issue.officeId.toString());

    if (issue.attachments && issue.attachments.length > 0) {
      issue.attachments.forEach((file) => {
        issueData.append('attachments', file, file.name);
      });
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`,
    });

    return this.http.post(`${this.issueApiUrl}/new`, issueData, {headers}).subscribe((res) => console.log(res));
  }

  getIssues(): Observable<Issue[]> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`,
    });

    return this.http.get<any>(`${this.issueApiUrl}/user`, { headers }).pipe(
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

  getIssueDetails(trackingNumber: string): any {
    return this.http.get<any>(`${this.issueApiUrl}/${trackingNumber}`);
  }

  getIssueReplies(issueId: number): any {
    return this.http.get<any>(`${this.issueReplyApiUrl}/${issueId}`);
  }

  postIssueReply(issueId: number, body: { comment: string; updatedStatus: string }): Observable<any>{
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`
    });
    return this.http.post(`${this.issueReplyApiUrl}/${issueId}`,body,{ headers });
  }

  filterByTrx(tracking_number: string, userType: number): any {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`
    });

    return this.http.post<any>(`${this.issueApiUrl}/${userType}/tracking/${tracking_number}`, null ,{ headers });
  }

  filterByStatus(status: string, userType: number): any {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`
    });

    return this.http.post<any>(`${this.issueApiUrl}/${userType}/status/${status}`, null, { headers });
  }

  filterByTextDesc(text_desc: string, userType: number): any {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`
    });

    return this.http.post<any>(`${this.issueApiUrl}/${userType}/find/${text_desc}`, null, { headers });
  }

  filterByDateRange(start_date: string, end_date: string, userType: number): any {
    const headers = new HttpHeaders({
    'Authorization': `Bearer ${this.authService.getToken()}`,
    });

    return this.http.post<any>(`${this.issueApiUrl}/${userType}/between_month/${start_date}/${end_date}`, null, { headers });
  }
}
