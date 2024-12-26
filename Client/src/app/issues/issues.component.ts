import {Component} from '@angular/core';
import {Issue} from '../../model/Issue.model';
import {IssueService} from '../services/issue.service';
import {Router} from '@angular/router';
import {IntercepterService} from '../services/intercepter.service';

@Component({
  selector: 'app-issues',
  templateUrl: './issues.component.html',
  styleUrl: './issues.component.scss',
})
export class IssuesComponent {

  status: boolean;
  constructor(
    private issueService: IssueService,
    private router: Router,
    private intercepter: IntercepterService
  ) {
    this.issueService.statusChanged.subscribe(statusChanged => {
      this.status = statusChanged;
    })
  }

  issues: Issue[] = [];

  // updateIssue(issue: Issue) {
  //   this.issues.push(issue);
  // }

  ngOnInit() {
    this.intercepter.validateRoutePermission();
    this.issues = this.issueService.getIssues();
    this.issueService.issueChanged.subscribe(issueList => {
      this.issues = issueList;
    })
  }

  openIssueDetails(i: string) {
    this.router.navigate(['issues', i]);
  }
}
