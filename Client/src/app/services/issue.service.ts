import {Issue} from '../../model/Issue.model';
import {EventEmitter} from '@angular/core';

export class IssueService {
  statusChanged: EventEmitter<boolean> = new EventEmitter();
  issueChanged = new EventEmitter<Issue[]>();

  private issues : Issue[] = [];
  status: boolean = false;

  createIssue(issue: Issue) {
    this.issues.push(issue);
    this.issueChanged.emit(this.issues.slice())
  }
  getIssues() {
    return this.issues.slice();
  }
}
