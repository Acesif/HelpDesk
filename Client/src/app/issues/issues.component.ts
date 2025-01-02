import {Component, ViewChild} from '@angular/core';
import {Issue} from '../../model/Issue.model';
import {IssueService} from '../services/issue.service';
import {NavigationEnd, Router} from '@angular/router';
import {IntercepterService} from '../services/intercepter.service';
import {filter} from 'rxjs';
import {FilterformComponent} from '../filterform/filterform.component';

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
    private intercepter: IntercepterService,
  ) {
    this.issueService.statusChanged.subscribe(statusChanged => {
      this.status = statusChanged;
    })
  }

  issues: Issue[] = [];

  // updateIssue(issue: Issue) {
  //   this.issues.push(issue);
  // }

  @ViewChild(FilterformComponent) filterFormComponent!: FilterformComponent;

  ngOnInit() {
    this.intercepter.validateRoutePermission();
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd && event.url === '/issues/list'))
      .subscribe(() => {
        this.loadIssues();
      });

    this.loadIssues();
  }

  getFilterDataFromChild() {
    const data = this.filterFormComponent.getFilterData();
    console.log(data);
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

  openIssueDetails(id: number) {
    this.router.navigate(['issues','details'], { queryParams: { id: id } });
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'OPEN':
        return 'text-primary';
      case 'RESOLVED':
        return 'text-success';
      case 'REJECTED':
        return 'text-danger';
      case 'ONGOING':
        return 'text-warning';
      case 'CLOSED':
        return 'text-secondary';
      default:
        return '';
    }
  }
}
