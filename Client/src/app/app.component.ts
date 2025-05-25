import { Component } from '@angular/core';
import {AuthService} from './services/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {LoginService} from './services/login.service';
import {JwtHelperService} from '@auth0/angular-jwt';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'

})
export class AppComponent {
  appName = 'GRS Helpdesk';
  private payload: {};

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private loginService: LoginService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    let token = this.route.snapshot.queryParamMap.get('token');

    if (!token) {
      const url = new URL(window.location.href);
      token = url.searchParams.get('token');
    }

    const jwtHelper = new JwtHelperService();

    if (token) {
      try {
        const cleanToken = token.trim();
        const decodedToken = jwtHelper.decodeToken(cleanToken);

        if (!decodedToken) throw new Error("Decoded token is null");

        const userInformation = decodedToken?.user_info;
        if (!userInformation) throw new Error("Missing user_info in token");

        if(userInformation.userType === "SYSTEM_USER") {
          this.payload = {
            name: userInformation?.username,
            username: userInformation?.username,
            officeId: 0,
            designation: "SUPERADMIN",
            password: "123456789",
            // officeMinistryId: userInformation?.officeInformation?.officeMinistryId,
            // officeOriginId: userInformation?.officeInformation?.officeOriginId,
            // role: userInformation?.officeInformation?.designation,
            // employeeRecordId: userInformation?.officeInformation?.employeeRecordId,
            // officeUnitOrganogramId: userInformation?.officeInformation?.officeUnitOrganogramId,
            // layerLevel: userInformation?.officeInformation?.layerLevel,
            // officeNameBangla: userInformation?.officeInformation?.officeNameBangla,
          };
        } else {
          this.payload = {
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
          };
        }

        const successfulRedirection = this.loginService.referredLoginFromGRS(this.payload);
        successfulRedirection.subscribe(res => {
          if (res.status === "OK") {
            this.authService.saveToken(res.data.token);
            this.router.navigate(['issues', 'list']);
          } else {
            window.location.href = "http://localhost/dashboard.do";
          }
        });

      } catch (error) {
        console.error("❌ Token decoding failed:", error);
        console.warn("🔎 Token string was:", token);
        window.location.href = "http://localhost/dashboard.do";
      }
    } else {
      console.warn("❌ No token found in query parameters");
      window.location.href = "http://localhost/dashboard.do";
    }
  }

}
