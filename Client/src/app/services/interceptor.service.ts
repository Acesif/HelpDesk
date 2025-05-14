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
    } else if (this.activeRoute.snapshot.queryParamMap.has('token')) {
      const jwtHelper = new JwtHelperService();
      const token = this.activeRoute.snapshot.queryParamMap.get('token');
      const userInformation = jwtHelper.decodeToken(token)?.user_info;
      const payload = {
        name: userInformation?.officeInformation?.name,
        username: userInformation?.username,
        officeId: userInformation?.officeInformation?.officeId,
        designation: userInformation?.oisfUserType,
        password: "123456789",
        officeMinistryId: userInformation?.officeInformation?.officeMinistryId,
        officeOriginId: userInformation?.officeInformation?.officeOriginId,
        role: userInformation?.officeInformation?.designation,
        employeeRecordId: userInformation?.officeInformation?.employeeRecordId,
        officeUnitOrganogramId: userInformation?.officeInformation?.officeUnitOrganogramId,
        layerLevel: userInformation?.officeInformation?.layerLevel,
        officeNameBangla: userInformation?.officeInformation?.officeNameBangla,
      }
      const successfulRedirection = this.loginService.referredLoginFromGRS(payload);
      successfulRedirection.subscribe(res => {
        if (res.status === "OK") {
          this.authService.saveToken(res.data.token);
          this.router.navigate(["admin","dashboard"]);
        } else {
          this.router.navigate(['auth','login']);
        }
      });
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
