import {Issue} from '../../model/Issue.model';
import {EventEmitter, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class IssueService {
  statusChanged: EventEmitter<boolean> = new EventEmitter();
  issueChanged = new EventEmitter<Issue[]>();

  private issues: Issue[] = [
    new Issue(
      "Network Outage",
      "There is a network outage in office 1 affecting all employees.",
      "office_1",
      "Network",
    ),
    new Issue(
      "System Downtime",
      "The payroll system was down during the weekend.",
      "office_2",
      "System",
    ),
    new Issue(
      "Broken Printer",
      "The printer in the third-floor office is broken and needs replacement.",
      "office_3",
      "Equipment",
    ),
    new Issue(
      "Power Outage",
      "A sudden power outage caused work delays in office 4.",
      "office_4",
      "Power",
    ),
    new Issue(
      "Security Breach",
      "Unauthorized access to the server detected.",
      "office_5",
      "Security",
    ),
  ];

  status: boolean = false;

  private apiUrl = 'http://localhost:7890/api/issue'; // Replace with your backend API URL

  constructor(private http: HttpClient) {}

  createIssue(issue: Issue): Observable<any> {
    const issueData = new FormData();

    issueData.append('title', issue.title);
    issueData.append('description', issue.description);
    issueData.append('category', issue.issueCategory);
    issueData.append('officeId', issue.office);

    if (issue.attachments && issue.attachments.length > 0) {
      issue.attachments.forEach((file) => {
        issueData.append('attachments', file, file.name);
      });
    }

    return this.http.post(`${this.apiUrl}/new`, issueData);
  }
  getIssues() {
    return this.issues.slice();
  }
}
