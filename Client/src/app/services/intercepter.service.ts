import { Injectable } from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class IntercepterService {

  constructor(
    private router: Router,
    private authService: AuthService,
  ) {}

  validateRoutePermission(){
    let isAuthenticated: boolean = this.authService.isAuthenticated();
    if (isAuthenticated) {
      let designation:string = this.authService.getUserDesignation();
      if (designation === 'GRO' || designation === 'VENDOR') {
        const currentRoute = this.router.url;
        if (currentRoute.includes("admin") || currentRoute.includes("issues")) {
          this.router.navigate([currentRoute]);
        } else {
          this.router.navigate(["admin","dashboard"]);
        }
      } else {
        const currentRoute = this.router.url;
        if (currentRoute.includes("issues") || currentRoute.includes("profile")) {
          this.router.navigate([currentRoute]);
        } else {
          this.router.navigate(["issues","list"]);
        }
      }
    } else {
      const currentRoute:string = this.router.url;
      if (currentRoute === '/auth/register' || currentRoute === '/auth/login') {
        this.router.navigate([currentRoute]);
      } else {
        this.router.navigate(['auth','login']);
      }
    }
  }
}
