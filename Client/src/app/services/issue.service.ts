import {Issue} from '../../model/Issue.model';
import {EventEmitter} from '@angular/core';

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

  createIssue(issue: Issue) {
    console.log(issue);
  }
  getIssues() {
    return this.issues.slice();
  }
}
