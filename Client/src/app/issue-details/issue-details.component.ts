import { Component } from '@angular/core';
import {Issue} from '../../model/Issue.model';
import {ActivatedRoute} from '@angular/router';
import {IssueService} from '../services/issue.service';

@Component({
  selector: 'app-issue-details',
  templateUrl: './issue-details.component.html',
  styleUrl: './issue-details.component.scss'
})
export class IssueDetailsComponent {
  issue: Issue;

  constructor(private route: ActivatedRoute, private issueService: IssueService) {}

  ngOnInit(){
    this.issue = this.issueService.getIssues().find(issue => this.route.snapshot.params['id'] === issue.tracking_number);
    this.route.params.subscribe(params => {
      this.issue = this.issueService.getIssues().find(issue => issue.tracking_number === params['id']);
    })
    // this.issue = {
    //   tracking_number: this.route.snapshot.params['id'],
    //   status: "",
    //   subject: "",
    //   office: "",
    //   date: ""
    // }
  }
}
