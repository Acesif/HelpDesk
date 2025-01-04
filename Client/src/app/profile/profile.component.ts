import { Component } from '@angular/core';
import {ProfileService} from '../services/profile.service';
import {ActivatedRoute} from '@angular/router';
import {User} from '../../model/user.model';
import {InterceptorService} from '../services/interceptor.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
  providers: [ProfileService]
})
export class ProfileComponent {
  user: User;

  constructor(
    private router: ActivatedRoute,
    private profileService: ProfileService,
    private interceptor: InterceptorService
  ) { }

  ngOnInit() {
    this.interceptor.validateRoutePermission();
    this.user = this.profileService.getUserInfo(this.router.snapshot.params['id']);
  }

}
