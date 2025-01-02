import { Component } from '@angular/core';
import {IntercepterService} from '../services/intercepter.service';
import {DashboardService} from '../services/dashboard.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.scss'
})
export class HomepageComponent {

  pieData: { name: string, value: number }[] = [];
  total: number;

  constructor(
    private intercepter: IntercepterService,
    private dashboardService: DashboardService,
  ) {}

  getValueByName(name: string): number | undefined {
    const entry: { name: string, value: number } = this.pieData.find(item => item.name === name);
    return entry ? entry.value : undefined;
  }

  getPercentByName(name: string): number | undefined {
    const entry: { name: string, value: number } = this.pieData.find(item => item.name === name);
    return entry ? (entry.value/this.total)*100 : undefined;
  }

  ngOnInit(): void {
    this.intercepter.validateRoutePermission();
    this.dashboardService.getDashboardData().subscribe((res: any) => {

      this.pieData = [
        { name: 'OPENED', value: res.data.open },
        { name: 'RESOLVED', value: res.data.resolved },
        { name: 'REJECTED', value: res.data.rejected },
        { name: 'ONGOING', value: res.data.ongoing },
        { name: 'CLOSED', value: res.data.closed },
      ];
      this.total = res.data.total;
    });
  }

  view: [number, number] = [700, 400];

  gradient: boolean = true;
  showLegend: boolean = false;
  showLabels: boolean = true;
  isDoughnut: boolean = true;
}
