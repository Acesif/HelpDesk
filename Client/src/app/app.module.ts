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
import { FilterformComponent } from './filterform/filterform.component';
import { TitleComponent } from './shared/title/title.component';
import { HomepageComponent } from './homepage/homepage.component';
import { ProfileComponent } from './profile/profile.component';
import { RegistrationComponent } from './registration/registration.component';
import { provideHttpClient } from '@angular/common/http';
import { LoginComponent } from './login/login.component';

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
      { path: 'inbox', component: IssuesComponent },
    ]
  },
  { path: 'profile/:id', component: ProfileComponent },
  { path: 'issues',
    children: [
      { path: 'new', component: IssueFormComponent },
      { path: 'list', component: IssuesComponent },
      { path: ':id', component: IssueDetailsComponent },
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
    FilterformComponent,
    TitleComponent,
    ProfileComponent,
    RegistrationComponent,
    LoginComponent,
  ],
  imports: [
    BrowserModule,
    NgOptimizedImage,
    FormsModule,
    NgxChartsModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot(routes)
  ],
  providers: [IssueService, provideHttpClient()],
  bootstrap: [AppComponent]
})
export class AppModule { }
