import {Component} from '@angular/core';
import {Issue} from '../../model/Issue.model';
import {ActivatedRoute} from '@angular/router';
import {IssueService} from '../services/issue.service';
import {IntercepterService} from '../services/intercepter.service';
import {AuthService} from '../services/auth.service';

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
    private intercepter: IntercepterService,
    private route: ActivatedRoute,
    private auth: AuthService,
  ) {}
  ngOnInit(): void {
    this.route.queryParamMap.subscribe((params) => {
      this.issueId = params.get('id');
      if (this.issueId) {
        this.fetchIssueDetails(this.issueId);
      }
    });
  }

  fetchIssueDetails(trackingNumber: string): void {
    this.issueService.getIssueDetails(trackingNumber).subscribe((issue: any) => {
      this.issue = new Issue(
        issue.data.trackingNumber,
        issue.data.title,
        issue.data.description,
        issue.data.category,
        issue.data.status,
        issue.data.officeId);
      }
    );
  }

  isAdminAndisNotSelf(): boolean {
    const designation = this.auth.getUserDesignation();
    const userId = this.auth.getUserId();
    return designation === 'GRO' || designation === 'VENDOR';
  }

  postReply() {

  }

  fetchIssueReplies(){}

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
