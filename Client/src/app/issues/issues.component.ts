import { Component } from '@angular/core';
import {Issue} from '../../model/Issue.model';

@Component({
  selector: 'app-issues',
  templateUrl: './issues.component.html',
  styleUrl: './issues.component.scss'
})
export class IssuesComponent {

  issues: Issue[] = [];

  updateIssue(issue: Issue) {
    this.issues.push(issue);
  }
}
