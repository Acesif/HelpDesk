import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { IssuesComponent } from './issues/issues.component';
import { NavbarComponent } from './navbar/navbar.component';
import { IssueDetailsComponent } from './issue-details/issue-details.component';
import { NgOptimizedImage } from '@angular/common';
import { IssueFormComponent } from './issue-form/issue-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DropdownDirective } from './shared/dropdown.directive';
import { IssueService } from './services/issue.service';
import { Route, RouterModule } from '@angular/router';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { FilterFormComponent } from './filterform/filter-form.component';
import { TitleComponent } from './shared/title/title.component';
import { HomepageComponent } from './homepage/homepage.component';
import { ProfileComponent } from './profile/profile.component';
import { RegistrationComponent } from './registration/registration.component';
import { provideHttpClient } from '@angular/common/http';
import { LoginComponent } from './login/login.component';
import { AdminInboxComponent } from './admin-inbox/admin-inbox.component';
import { LoadingSpinnerComponent } from './loading-spinner/loading-spinner.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ToastrModule} from 'ngx-toastr';

const routes: Route[] = [
  { path: '', component: RegistrationComponent },
  { path: 'auth',
    children: [
      { path: 'login', component: LoginComponent },
      { path: 'register', component: RegistrationComponent },
    ]
  },
  { path: 'admin',
    children: [
      { path: 'dashboard', component: HomepageComponent },
      { path: 'inbox', component: AdminInboxComponent },
    ]
  },
  { path: 'profile', component: ProfileComponent },
  { path: 'issues',
    children: [
      { path: 'new', component: IssueFormComponent },
      { path: 'list', component: IssuesComponent },
      { path: 'details', component: IssueDetailsComponent },
    ]
  },
];


@NgModule({
  declarations: [
    AppComponent,
    IssuesComponent,
    NavbarComponent,
    IssueDetailsComponent,
    IssueFormComponent,
    DropdownDirective,
    HomepageComponent,
    FilterFormComponent,
    TitleComponent,
    ProfileComponent,
    RegistrationComponent,
    LoginComponent,
    AdminInboxComponent,
    LoadingSpinnerComponent,
  ],
  imports: [
    BrowserModule,
    NgOptimizedImage,
    FormsModule,
    NgxChartsModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot(routes),
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      positionClass: 'toast-top-right',
      preventDuplicates: true,
      timeOut: 2000,
    }),
  ],
  providers: [IssueService, provideHttpClient()],
  bootstrap: [AppComponent]
})
export class AppModule { }
