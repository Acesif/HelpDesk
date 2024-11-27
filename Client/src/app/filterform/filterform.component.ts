import { Component } from '@angular/core';

@Component({
  selector: 'app-filterform',
  templateUrl: './filterform.component.html',
  styleUrl: './filterform.component.scss'
})
export class FilterformComponent {
  startDate: string;
  endDate: string;
  trackingNumber: number;
  textdesc: string;
  status: string;

  constructor() {
    this.status = "";
    this.textdesc = "";
  }


  ngOnInit() {
    this.startDate = new Date().toISOString().slice(0, 7);
    this.endDate = new Date().toISOString().slice(0, 7);
  }
  onSubmit(formData: any) {
    console.log(formData);
  }
}
