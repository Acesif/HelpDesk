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
  title: string = '';
  description: string = '';
  category: string = '';
  officeId: number = null;
  attachments: File[] = [];

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      this.attachments = Array.from(input.files);
    }
  }

  categories: string[] = [
    'REPORT',
    'SUBMISSION',
    'LOGIN',
    'ACTION',
    'OTHERS'
  ];

  ngOnInit(): void {
    this.intercepter.validateRoutePermission();
  }

  // validateForm(formData: any): [boolean,string[]] {
  //   let emptyFields: string[] = [];
  //
  //   if (formData.subject === '') emptyFields.push("title");
  //   if (formData.description === '') emptyFields.push("description");
  //   if (formData.category === '') emptyFields.push("category");
  //   if (formData.office === '') emptyFields.push("office");
  //
  //   if (emptyFields.length > 0) {
  //     return [false,emptyFields]
  //   } else {
  //     this.title = formData.title;
  //     this.description = formData.description;
  //     this.category = formData.category;
  //     this.officeId = formData.officeId;
  //     return [true,emptyFields]
  //   }
  // }

  onSubmit(formData: any): void {

    // const [isValid, emptyFields]: [boolean, string[]] = this.validateForm(formData);
    // if (isValid) {

      // if (emptyFields.length > 0) {
      //   alert(`please fill up these fields`);
      // }

      let issue: Issue = new Issue(
        "123456789",
        formData.value.title,
        formData.value.description,
        formData.value.category,
        formData.value.status,
        formData.value.officeId,
        this.attachments
      );

      this.issueService.createIssue(issue);
      this.resetFormData();
      this.router.navigate(['/issues','list']);
    // }
  }

  resetFormData(){
    this.title = '';
    this.description = '';
    this.category = '';
    this.officeId = null;
    this.attachments = [];

    const fileInput = document.getElementById('attachments') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
  }
}
