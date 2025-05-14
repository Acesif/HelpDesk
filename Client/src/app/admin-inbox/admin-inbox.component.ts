import { Component } from '@angular/core';
import {Issue} from '../../model/Issue.model';
import {AuthService} from '../services/auth.service';
import {AdminService} from '../services/admin.service';
import {filter, forkJoin} from 'rxjs';
import {NavigationEnd, Router} from '@angular/router';
import {InterceptorService} from '../services/interceptor.service';
import {IssueService} from '../services/issue.service';

@Component({
  selector: 'app-admin-inbox',
  templateUrl: './admin-inbox.component.html',
  styleUrl: './admin-inbox.component.scss'
})
export class AdminInboxComponent {

  issues: Issue[] = [];
  allIssues: Issue[] = [];
  loading: boolean = true;
  page: number = 0;

  constructor(
    private adminService: AdminService,
    private router: Router,
    private interceptor: InterceptorService,
    private issueService: IssueService
  ) {}

  ngOnInit() {
    this.interceptor.validateRoutePermission();
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd && event.url === '/admin/inbox'))
      .subscribe(() => {
        this.loadIssues(this.page);
      });
    this.loadIssues(this.page);
  }

  loadIssues(page: number): void {
    this.adminService.getInboxIssues(page).subscribe(
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

  openInboxIssue(id: number) {
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
      requests.push(this.issueService.filterByTrx(filter.tracking_number, 0));
    }

    if (filter.status) {
      requests.push(this.issueService.filterByStatus(filter.status, 0));
    }

    if (filter.start_date && filter.end_date) {
      requests.push(this.issueService.filterByDateRange(filter.start_date, filter.end_date, 0));
    }

    if (filter.text_desc) {
      requests.push(this.issueService.filterByTextDesc(filter.text_desc, 0));
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
    }, error => {
      console.error('Error fetching filtered issues:', error);
    });
  }

  setPage(number: number) {
    if(number === 0 && !(this.page === 0)){
      this.page -= 1;
      this.loadIssues(this.page);
    } else if (number === 1) {
      this.page += 1;
      this.loadIssues(this.page);
    }
  }
}
