import {Component} from '@angular/core';
import {Issue} from '../../model/Issue.model';
import {ActivatedRoute, Router} from '@angular/router';
import {IssueService} from '../services/issue.service';
import {AuthService} from '../services/auth.service';
import {ProfileService} from '../services/profile.service';
import {log} from '@angular-devkit/build-angular/src/builders/ssr-dev-server';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';

@Component({
  selector: 'app-issue-details',
  templateUrl: './issue-details.component.html',
  styleUrl: './issue-details.component.scss'
})
export class IssueDetailsComponent {
  issue: Issue;
  issueId: any;
  replies: Array<any> = [];
  newReply: { message: string, status: string } = { message: '', status: '' };
  loading: boolean = true;
  previewUrl: SafeUrl | null = null;
  previewType: string = '';
  isPreviewVisible = false;

  constructor(
    private issueService: IssueService,
    private sanitizer: DomSanitizer,
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
      case 'OPEN':
        return 'badge bg-primary text-white';
      case 'RESOLVED':
        return 'badge bg-success text-white';
      case 'REJECTED':
        return 'badge bg-danger text-white';
      case 'ONGOING':
        return 'badge bg-warning text-dark';
      default:
        return 'badge bg-secondary text-white';
    }
  }

  fetchIssueDetails(trackingNumber: string): void {
    this.issueService.getIssueDetails(trackingNumber).subscribe((issue: any) => {
      this.issueService.getAttachments(issue.data.id).subscribe({
        next: (attachment: any) => {
          this.issue = new Issue(
            issue.data.id,
            issue.data.trackingNumber,
            issue.data.title,
            issue.data.description,
            issue.data.category,
            issue.data.status,
            null,
            issue.data.postedOn,
            issue.data.postedBy,
            issue.data.updatedOn,
            attachment.data,
          );
          console.log(this.issue);
        },
        error: (err: any) => {
          console.error('Error fetching attachments:', err);
        },
      });
      this.loading = false;
      }
    );
  }

  isAdminAndIsNotSelf(): boolean {
    const designation = this.auth.getUserDesignation();
    return designation === 'GRO' || designation === 'VENDOR';
  }

  fetchIssueReplies(issueId: number): void {
    this.issueService.getIssueReplies(issueId).subscribe(
      (response: any) => {
        this.replies = response.data;

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
        this.loading = false;
        console.log('Replies with user info:', this.replies);
      },
      (error: any) => {
        console.error('Error fetching replies:', error);
      }
    );
  }

  postReply(): void {
    let requestBody = {
      comment: this.newReply.message,
      updatedStatus: this.newReply.status,
    };

    this.issueService.postIssueReply(this.issueId, requestBody).subscribe(
      () => {
        this.fetchIssueDetails(this.issueId);
        this.fetchIssueReplies(this.issueId);

        this.newReply.message = '';
        this.newReply.status = '';
      },
      (error: any) => {
        console.error('Error posting reply:', error);
      }
    );
  }

  getStatusColor(): string {
    switch (this.newReply.status) {
      case 'OPEN':
        return 'bg-primary text-white';
      case 'RESOLVED':
        return 'bg-success text-white';
      case 'REJECTED':
        return 'bg-danger text-white';
      case 'ONGOING':
        return 'bg-warning text-dark';
      case 'CLOSED':
        return 'bg-secondary text-white';
      default:
        return '';
    }
  }

  downloadFile(fileName: string, id: number): void {
    this.issueService.getFile(fileName, id);
  }

  previewFile(id: number): void {
    this.issueService.getFilePreview(id).subscribe(
      (blob) => {
        const mimeType = blob.type;

        // Set preview type based on MIME type
        if (mimeType.startsWith('image/')) {
          this.previewType = 'image';
        } else if (mimeType === 'application/pdf') {
          this.previewType = 'pdf';
        } else {
          this.previewType = 'other';
        }

        const url = window.URL.createObjectURL(blob);
        this.previewUrl = this.sanitizer.bypassSecurityTrustUrl(url);

        // Show modal
        this.isPreviewVisible = true;
      },
      (error) => {
        console.error('Error previewing file:', error);
      }
    );
  }

  closePreview(): void {
    this.isPreviewVisible = false;
    this.previewUrl = null;
    this.previewType = '';
  }
}
