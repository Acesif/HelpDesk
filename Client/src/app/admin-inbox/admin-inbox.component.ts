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
  replies: Array<any> = [];
  newReply = { message: '', postedBy: 'You', postedOn: new Date(), status: '' };

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


  isAdminAndisNotSelf(): boolean {
    const designation = this.auth.getUserDesignation();
    return designation === 'GRO' || designation === 'VENDOR';
  }

  postReply() {

  }

  fetchIssueReplies(){

  }

  getStatusColor(): string {
    switch (this.newReply.status) {
      case 'OPENED':
        return 'bg-primary text-white';
      case 'RESOLVED':
        return 'bg-success text-white';
      case 'REJECTED':
        return 'bg-danger text-white';
      case 'PENDING':
        return 'bg-warning text-dark';
      default:
        return '';
    }
  }

  openInboxIssue(id: number) {
    this.router.navigate(['issues','details'], { queryParams: { id: id } });
  }
}
