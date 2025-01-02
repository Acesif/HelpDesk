import { Component } from '@angular/core';
import {Issue} from '../../model/Issue.model';
import {AuthService} from '../services/auth.service';
import {AdminService} from '../services/admin.service';
import {filter} from 'rxjs';
import {NavigationEnd, Router} from '@angular/router';
import {IntercepterService} from '../services/intercepter.service';

@Component({
  selector: 'app-admin-inbox',
  templateUrl: './admin-inbox.component.html',
  styleUrl: './admin-inbox.component.scss'
})
export class AdminInboxComponent {

  issues: Issue[] = [];

  constructor(
    private adminService: AdminService,
    private router: Router,
    private intercepter: IntercepterService,
    private auth: AuthService,
  ) {}

  ngOnInit() {
    this.intercepter.validateRoutePermission();
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd && event.url === '/admin/inbox'))
      .subscribe(() => {
        this.loadIssues();
      });
    this.loadIssues();
  }

  loadIssues(): void {
    this.adminService.getInboxIssues().subscribe(
      (issues: Issue[]) => {
        this.issues = issues;
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
}
