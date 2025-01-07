import { Component } from '@angular/core';
import {InterceptorService} from '../services/interceptor.service';
import {DashboardService} from '../services/dashboard.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.scss'
})
export class HomepageComponent {

  pieData: { name: string, value: number }[] = [];
  total: number;
  loading: boolean = true;

  constructor(
    private interceptor: InterceptorService,
    private dashboardService: DashboardService,
  ) {}

  getValueByName(name: string): number | undefined {
    const entry: { name: string, value: number } = this.pieData.find(item => item.name === name);
    return entry ? entry.value : undefined;
  }

  getPercentByName(name: string): number | undefined {
    const entry: { name: string, value: number } = this.pieData.find(item => item.name === name);
    return entry ? parseFloat((entry.value * 100 / this.total).toFixed(2)) : undefined;
  }

  ngOnInit(): void {
    this.interceptor.validateRoutePermission();
    this.dashboardService.getDashboardData().subscribe((res: any) => {
      this.loading = false;

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
