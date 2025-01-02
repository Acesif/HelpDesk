import { Component } from '@angular/core';
import {AuthService} from './services/auth.service';
import {Router} from '@angular/router';
import {LoginService} from './services/login.service';
import {map} from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'

})
export class AppComponent {
  appName = 'GRS Helpdesk';

  constructor(
    private authService: AuthService,
    private router: Router,
    private loginService: LoginService,
  ) {}

  ngOnInit() {
    const unixTimestamp: number = Math.floor(Date.now() / 1000);
    if (unixTimestamp > this.authService.getValidity()) {
      this.loginService.refreshToken(this.authService.getToken())
        .subscribe((response: any) => {
          this.authService.saveToken(response.data.refreshToken);
        })
    }
  }
}
