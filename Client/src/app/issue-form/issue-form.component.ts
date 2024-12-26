import {Component, EventEmitter, Output, SimpleChanges} from '@angular/core';
import {Issue} from '../../model/Issue.model';
import {IssueService} from '../services/issue.service';
import {ActivatedRoute, Router} from '@angular/router';
import {IntercepterService} from '../services/intercepter.service';

@Component({
  selector: 'app-issue-form',
  templateUrl: './issue-form.component.html',
  styleUrl: './issue-form.component.scss',
})

export class IssueFormComponent {

  constructor(
    private issueService: IssueService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private intercepter: IntercepterService
  ) {}

  trackingNumber: string = '';
  subject: string = '';
  status: string = '';
  office: string = '';
  date: string = '';

  statuses: string[] = [
    'OPEN',
    'RESOLVED',
    'REJECTED',
    'ONGOING',
    'CLOSED'
  ];

  ngOnInit(): void {
    this.intercepter.validateRoutePermission();
    this.generateTrackingNumber();
  }

  generateTrackingNumber(): void {
    this.trackingNumber = `${Date.now()}${Math.floor(Math.random() * 1000)}`;
  }

  validateForm(formData: any): [boolean,string[]] {
    let emptyFields: string[] = [];

    if (!formData.subject) emptyFields.push("Subject");
    if (!formData.status) emptyFields.push("Status");
    if (!formData.office) emptyFields.push("Office");
    if (!formData.date) emptyFields.push("Date");

    if (emptyFields.length > 0) {
      return [false,emptyFields]
    } else {
      return [true,emptyFields]
    }
  }

  // @Output() issueDataEmitter: EventEmitter<Issue> = new EventEmitter();

  onSubmit(formData: any): void {
    const [isValid, emptyFields]:[boolean,string[]] = this.validateForm(formData);
    if (isValid){

      // this.issueDataEmitter.emit(
      //     new Issue(
      //       formData.trackingNumber,
      //       formData.subject,
      //       formData.status,
      //       formData.office,
      //       formData.date
      //     )
      // )

      this.issueService.createIssue(
            new Issue(
              formData.trackingNumber,
              formData.subject,
              formData.status,
              formData.office,
              formData.date
            )
      )
      this.resetFormData()
      this.issueService.statusChanged.emit(true);
      this.router.navigate(['/issues/list'], { relativeTo: this.activatedRoute })
        .then(() => {
        })
        .catch((err) => {
          console.log(err.message);
        });

    } else {
      alert(`The following fields are required: ${emptyFields.join(', ')}`);
    }
  }

  resetFormData(){
    this.trackingNumber = '';
    this.subject = '';
    this.status = '';
    this.office = '';
    this.date = '';
    this.generateTrackingNumber();
  }
}
