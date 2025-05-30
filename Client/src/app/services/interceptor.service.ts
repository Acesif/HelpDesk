import { Injectable } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from './auth.service';
import {LoginService} from './login.service';
import {JwtHelperService} from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class InterceptorService {

  constructor(
    private router: Router,
    private authService: AuthService,
    private activeRoute: ActivatedRoute,
    private loginService: LoginService
  ) {}

  validateRoutePermission(){
    let isAuthenticated: boolean = this.authService.isAuthenticated();
    if (isAuthenticated) {
      let designation:string = this.authService.getUserDesignation();
      if (designation === 'GRO' || designation === 'VENDOR') {
        const currentRoute = this.router.url;
        if
        (
          currentRoute.includes("admin") ||
          currentRoute.includes("issues") ||
          currentRoute.includes("profile")
        ) {
          this.router.navigate([currentRoute]);
        } else {
          this.router.navigate(["admin","dashboard"]);
        }
      } else {
        const currentRoute = this.router.url;
        if
        (
          currentRoute.includes("issues") ||
          currentRoute.includes("profile")
        ) {
          this.router.navigate([currentRoute]);
        } else {
          this.router.navigate(["issues","list"]);
        }
      }
    }
  }
}
