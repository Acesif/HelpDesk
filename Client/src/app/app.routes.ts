import { Routes } from '@angular/router'
import {IssueFormComponent} from './issue-form/issue-form.component';
import {IssuesComponent} from './issues/issues.component';

export const routes: Routes = [
  {
    path: 'issues-new',
    component: IssueFormComponent
  },
  {
    path: '',
    component: IssuesComponent
  }
]
