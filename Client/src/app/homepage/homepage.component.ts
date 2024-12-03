import { Component } from '@angular/core';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.scss'
})
export class HomepageComponent {
  view: [number, number] = [700, 400];

  pieData: {name: string, value: number}[] = [
    { name: 'OPENED', value: 1 },
    { name: 'RESOLVED', value: 20 },
    { name: 'REJECTED', value: 30 },
    { name: 'PENDING', value: 10 },
  ];

  gradient: boolean = true;
  showLegend: boolean = false;
  showLabels: boolean = true;
  isDoughnut: boolean = true;
}
