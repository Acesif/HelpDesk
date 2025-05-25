import {Component, ViewChild} from '@angular/core';
import {Issue} from '../../model/Issue.model';
import {IssueService} from '../services/issue.service';
import {NavigationEnd, Router} from '@angular/router';
import {InterceptorService} from '../services/interceptor.service';
import {filter} from 'rxjs';

@Component({
  selector: 'app-issues',
  templateUrl: './issues.component.html',
  styleUrl: './issues.component.scss',
})
export class IssuesComponent {

  status: boolean;
  readonly PAGE_SIZE: number = 10;
  hasNextPage: boolean = true;

  issues: Issue[] = [];
  allIssues: Issue[] = [];
  page: number = 0;
  loading: boolean = true;
  currentFilterState: any = {};

  constructor(
    private issueService: IssueService,
    private router: Router,
    private interceptor: InterceptorService,
  ) {
    this.issueService.statusChanged.subscribe(statusChanged => {
      this.status = statusChanged;
    })
  }

  ngOnInit() {
    this.interceptor.validateRoutePermission();
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd && event.url === '/issues/list'))
      .subscribe(() => {
        this.loadIssues(this.page);
      });

    this.loadIssues(this.page);
  }

  loadIssues(page: number): void {
    this.loading = true;
    this.issueService.getIssues(page).subscribe(
      (issues: Issue[]) => {
        this.allIssues = issues; // Store the fetched unfiltered page
        this.applyCurrentFilters(); // Apply any active filters to the new data
        this.hasNextPage = issues.length === this.PAGE_SIZE;
        this.loading = false;
      },
      (error) => {
        console.error('Error loading issues:', error);
        this.issues = [];
        this.allIssues = [];
        this.hasNextPage = false;
        this.loading = false;
      }
    );
  }

  applyCurrentFilters(): void {
    if (Object.values(this.currentFilterState).every(val => !val || (Array.isArray(val) && val.length === 0))) {
      this.issues = [...this.allIssues];
      return;
    }

    console.log('Applied Filter:', this.currentFilterState);  // Debug log

    this.issues = this.allIssues.filter(issue => {
      let matchesTrackingNumber = false;
      if (this.currentFilterState.tracking_number) {
        matchesTrackingNumber = issue.trackingNumber?.toString() === this.currentFilterState.tracking_number?.toString();
      }

      let matchesStatus = false;
      if (this.currentFilterState.status) {
        matchesStatus = issue.status === this.currentFilterState.status;
      }

      let matchesDateRange = false;
      if (this.currentFilterState.start_date && this.currentFilterState.end_date && issue.postedOn) {
        const issueYearMonth = issue.postedOn.slice(0, 7); // Extract "YYYY-MM"
        matchesDateRange = issueYearMonth >= this.currentFilterState.start_date &&
          issueYearMonth <= this.currentFilterState.end_date;
      }

      let matchesTextDesc = false;
      if (this.currentFilterState.text_desc) {
        const searchText = this.currentFilterState.text_desc.toLowerCase();
        matchesTextDesc = (issue.title?.toLowerCase()?.includes(searchText) ||
          issue.description?.toLowerCase()?.includes(searchText));
      }

      return matchesTrackingNumber || matchesStatus || matchesDateRange || matchesTextDesc;
    });
  }

  onFilterChange(filterData: any): void {
    this.loading = true;
    this.currentFilterState = filterData;
    // We are filtering the data already loaded for the current page (this.allIssues)
    this.applyCurrentFilters();
    this.loading = false;
    // hasNextPage is based on the unfiltered list, so it remains correct.
    // If all items on the current page are filtered out, this.issues will be empty,
    // but hasNextPage could still be true, allowing navigation to an unfiltered next page.
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

  setPage(direction: number) { // direction: 0 for previous, 1 for next
    if(direction === 0 && this.page > 0){
      this.page -= 1;
      this.loadIssues(this.page);
    } else if (direction === 1 && this.hasNextPage) {
      this.page += 1;
      this.loadIssues(this.page);
    }
  }
}
