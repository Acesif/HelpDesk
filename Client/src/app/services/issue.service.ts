import {Issue} from '../../model/Issue.model';
import {EventEmitter} from '@angular/core';

export class IssueService {
  statusChanged: EventEmitter<boolean> = new EventEmitter();
  issueChanged = new EventEmitter<Issue[]>();

  private issues : Issue[] = [
    new Issue("123456","asdfasdf","OPENED","office_1","21-12-2000"),
    new Issue("845746","iureoituioer","RESOLVED","office_2","11-10-2010"),
    new Issue("542135","ierouisdfg","CLOSED","office_3","21-03-2050"),
  ];
  status: boolean = false;

  createIssue(issue: Issue) {
    this.issues.push(issue);
    this.issueChanged.emit(this.issues.slice())
  }
  getIssues() {
    return this.issues.slice();
  }
}
