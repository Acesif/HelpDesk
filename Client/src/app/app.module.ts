import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { IssuesComponent } from './issues/issues.component';
import { NavbarComponent } from './navbar/navbar.component';
import { IssueDetailsComponent } from './issue-details/issue-details.component';
import {NgOptimizedImage} from '@angular/common';

@NgModule({
  declarations: [
    AppComponent,
    IssuesComponent,
    NavbarComponent,
    IssueDetailsComponent
  ],
  imports: [
    BrowserModule,
    NgOptimizedImage
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
