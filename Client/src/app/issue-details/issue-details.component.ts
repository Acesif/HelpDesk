import {Component} from '@angular/core';
import {Issue} from '../../model/Issue.model';
import {ActivatedRoute} from '@angular/router';
import {IssueService} from '../services/issue.service';
import {AuthService} from '../services/auth.service';
import {ProfileService} from '../services/profile.service';

@Component({
  selector: 'app-issue-details',
  templateUrl: './issue-details.component.html',
  styleUrl: './issue-details.component.scss'
})
export class IssueDetailsComponent {
  issue: Issue;
  issueId: any;
  replies: Array<any> = [];
  newReply = { message: '', postedBy: 'You', postedOn: new Date(), status: '' };

  constructor(
    private issueService: IssueService,
    private route: ActivatedRoute,
    private auth: AuthService,
    private profileService: ProfileService,
  ) {}

  ngOnInit(): void {
    this.route.queryParamMap.subscribe((params) => {
      this.issueId = params.get('id');
      if (this.issueId) {
        this.fetchIssueDetails(this.issueId);
        this.fetchIssueReplies(this.issueId);
      }
    });
  }

  getStatusLabelClass(status: string): string {
    switch (status) {
      case 'OPENED':
        return 'badge bg-primary text-white';
      case 'RESOLVED':
        return 'badge bg-success text-white';
      case 'REJECTED':
        return 'badge bg-danger text-white';
      case 'PENDING':
        return 'badge bg-warning text-dark';
      default:
        return 'badge bg-secondary text-white';
    }
  }

  fetchIssueDetails(trackingNumber: string): void {
    this.issueService.getIssueDetails(trackingNumber).subscribe((issue: any) => {
      this.issue = new Issue(
        issue.data.id,
        issue.data.trackingNumber,
        issue.data.title,
        issue.data.description,
        issue.data.category,
        issue.data.status,
        issue.data.officeId,
        issue.data.postedOn,
        issue.data.postedBy,
        issue.data.updatedOn
        );
      }
    );
  }

  isAdminAndisNotSelf(): boolean {
    const designation = this.auth.getUserDesignation();
    return designation === 'GRO' || designation === 'VENDOR';
  }

  fetchIssueReplies(issueId: number): void {
    this.issueService.getIssueReplies(issueId).subscribe(
      (response: any) => {
        this.replies = response.data;

        // Fetch user info for each reply
        this.replies.forEach((reply) => {
          this.profileService.getUserInfo(reply.repliantId).subscribe(
            (userInfo: any) => {
              reply.repliantName = userInfo.data.name;
            },
            (error: any) => {
              console.error(`Error fetching user info for repliantId ${reply.repliantId}`, error);
              reply.repliantName = 'Unknown User';
            }
          );
        });

        console.log('Replies with user info:', this.replies);
      },
      (error: any) => {
        console.error('Error fetching replies:', error);
      }
    );
  }

  postReply() {

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

}
