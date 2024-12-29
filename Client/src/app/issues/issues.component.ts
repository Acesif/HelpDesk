import {Component} from '@angular/core';
import {Issue} from '../../model/Issue.model';
import {IssueService} from '../services/issue.service';
import {NavigationEnd, Router} from '@angular/router';
import {IntercepterService} from '../services/intercepter.service';
import {filter} from 'rxjs';

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
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd && event.url === '/issues/list'))
      .subscribe(() => {
        this.loadIssues();
      });

    this.loadIssues();
  }

  loadIssues(): void {
    this.issueService.getIssues().subscribe(
      (issues: Issue[]) => {
        this.issues = issues;
      },
      (error) => {
        console.error('Error loading issues:', error);
      }
    );
  }

  openIssueDetails(id: string) {
    this.router.navigate(['issues','details'], { queryParams: { id: id } });
  }
}
