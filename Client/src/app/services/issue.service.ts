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

  createIssue(issue: Issue): Observable<any> {
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

    return this.http.post(`${this.issueApiUrl}/new`, issueData, {headers});
  }

  getIssues(page: number): Observable<Issue[]> {

    if (this.authService.getUserDesignation() === "SUPERADMIN") {

      const headers = new HttpHeaders({
        'Authorization': `Bearer ${this.authService.getToken()}`,
        'Access-Control-Allow-Origin': '*'
      });

      return this.http.get<any>(`${this.issueApiUrl}/all?page=${page}&size=10`, { headers }).pipe(
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
                  null,
                )
            );
          } else {
            console.error('Failed to retrieve issues:', res.message);
            return [];
          }
        })
      );
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`,
    });


    return this.http.get<any>(`${this.issueApiUrl}/user?page=${page}&size=10`, { headers }).pipe(
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
                null,
              )
          );
        } else {
          console.error('Failed to retrieve issues:', res.message);
          return [];
        }
      })
    );
  }

  getAttachments(issueId: number): any {
    return this.http.get<any>(`${this.ipConfigService.getAddress()}/api/attachments/issue/${issueId}`);
  }

  getFile(fileName: string, id: number): any {
    const url = `${this.ipConfigService.getAddress()}/api/attachments/${id}`;
    this.http
      .get(url, { responseType: 'blob' })
      .subscribe(
        (response) => {
          this.downloadFile(response, `${fileName}`);
        },
        (error) => {
          console.error('Error downloading file:', error);
        }
      );
  }

  private downloadFile(blob: Blob, fileName: string): void {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = fileName;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  }

  getFilePreview(id: number): Observable<Blob> {
    const url = `${this.ipConfigService.getAddress()}/api/attachments/${id}`;
    return this.http.get(url, { responseType: 'blob' });
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
