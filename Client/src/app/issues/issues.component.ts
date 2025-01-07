import {Component, ViewChild} from '@angular/core';
import {Issue} from '../../model/Issue.model';
import {IssueService} from '../services/issue.service';
import {NavigationEnd, Router} from '@angular/router';
import {InterceptorService} from '../services/interceptor.service';
import {filter, forkJoin} from 'rxjs';
import {FilterFormComponent} from '../filterform/filter-form.component';
import {log} from '@angular-devkit/build-angular/src/builders/ssr-dev-server';

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
    private interceptor: InterceptorService,
  ) {
    this.issueService.statusChanged.subscribe(statusChanged => {
      this.status = statusChanged;
    })
  }

  issues: Issue[] = [];
  allIssues: Issue[] = [];

  // updateIssue(issue: Issue) {
  //   this.issues.push(issue);
  // }

  loading: boolean = true;

  ngOnInit() {
    this.interceptor.validateRoutePermission();
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
        this.allIssues = issues;
        this.issues = [...issues];
        this.loading = false;
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

  onFilterChange(filter: any): void {
    const requests: any[] = [];

    if (filter.tracking_number) {
      requests.push(this.issueService.filterByTrx(filter.tracking_number, 1));
    }

    if (filter.status) {
      requests.push(this.issueService.filterByStatus(filter.status, 1));
    }

    if (filter.start_date && filter.end_date) {
      requests.push(this.issueService.filterByDateRange(filter.start_date, filter.end_date, 1));
    }

    if (filter.text_desc) {
      requests.push(this.issueService.filterByTextDesc(filter.text_desc, 1));
    }

    forkJoin(requests).subscribe((responses: any[]) => {
      const issuesByTracking_number: Issue[] = responses[0]?.data || [];
      const issuesByStatus: Issue[] = responses[1]?.data || [];
      const issuesByDateRange: Issue[] = responses[2]?.data || [];
      const issuesByTextDesc: Issue[] = responses[3]?.data || [];

      this.issues = this.allIssues.filter(issue =>
        (!issuesByTracking_number.length || issuesByTracking_number.some(filteredIssue => filteredIssue.id === issue.id)) &&
        (!issuesByStatus.length || issuesByStatus.some(filteredIssue => filteredIssue.id === issue.id)) &&
        (!issuesByDateRange.length || issuesByDateRange.some(filteredIssue => filteredIssue.id === issue.id)) &&
        (!issuesByTextDesc.length || issuesByTextDesc.some(filteredIssue => filteredIssue.id === issue.id))
      );
      if (this.issues.length === this.allIssues.length) {
        const noFilterActive = !filter.tracking_number && !filter.status && !filter.start_date && !filter.end_date && !filter.text_desc;
        if (!noFilterActive) {
          this.issues = [];
        }
      }
    }, error => {
      console.error('Error fetching filtered issues:', error);
    });
  }
}
