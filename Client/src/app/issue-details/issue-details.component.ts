import {Component} from '@angular/core';
import {Issue} from '../../model/Issue.model';
import {ActivatedRoute} from '@angular/router';
import {IssueService} from '../services/issue.service';
import {IntercepterService} from '../services/intercepter.service';

@Component({
  selector: 'app-issue-details',
  templateUrl: './issue-details.component.html',
  styleUrl: './issue-details.component.scss'
})
export class IssueDetailsComponent {
  issue: Issue;
  issueId: any;

  constructor(
    private issueService: IssueService,
    private intercepter: IntercepterService,
    private route: ActivatedRoute
  ) {}
  ngOnInit(): void {
    this.route.queryParamMap.subscribe((params) => {
      this.issueId = params.get('id');
      if (this.issueId) {
        this.fetchIssueDetails(this.issueId);
      }
    });
  }

  fetchIssueDetails(issueId: string): void {
    this.issueService.getIssueDetails(issueId).subscribe((issue: any) => {
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
}
