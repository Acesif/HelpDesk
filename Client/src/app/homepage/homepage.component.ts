import { Component } from '@angular/core';
import {IntercepterService} from '../services/intercepter.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.scss'
})
export class HomepageComponent {

  constructor(private intercepter: IntercepterService) {}

  ngOnInit(): void {
    this.intercepter.validateRoutePermission();
  }

  view: [number, number] = [700, 400];

  pieData: {name: string, value: number}[] = [
    { name: 'OPENED', value: 1 },
    { name: 'RESOLVED', value: 1 },
    { name: 'REJECTED', value: 1 },
    { name: 'ONGOING', value: 1 },
  ];

  gradient: boolean = true;
  showLegend: boolean = false;
  showLabels: boolean = true;
  isDoughnut: boolean = true;
}
