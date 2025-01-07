import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'app-filterform',
  templateUrl: './filter-form.component.html',
  styleUrl: './filter-form.component.scss'
})
export class FilterFormComponent {
  startDate: string;
  endDate: string;
  trackingNumber: number;
  textDesc: string;
  status: string;
  maxDate: string;
  dateError: boolean = false;

  constructor() {
    this.status = "";
    this.textDesc = "";
  }

  ngOnInit() {
    const date = new Date();
    date.setMonth(date.getMonth() - 6);
    this.startDate = date.toISOString().slice(0, 7);
    this.endDate = new Date().toISOString().slice(0, 7);
    this.maxDate = new Date().toISOString().slice(0, 7);
  }

  // validateDates(): boolean {
  //   if (this.startDate && this.endDate) {
  //     const dateError: boolean = this.startDate > this.endDate;
  //     if (dateError) {
  //       this.dateError = true;
  //       setTimeout(() => {
  //         this.dateError = false;
  //         this.endDate = new Date().toISOString().slice(0, 7);
  //       }, 2000);
  //     } else {
  //       this.dateError = false;
  //     }
  //   } else {
  //     this.dateError = false;
  //   }
  // }

  validateDates(): boolean {
    if (this.startDate && this.endDate) {
      return this.startDate > this.endDate;
    }
  }

  @Output() filterChanged = new EventEmitter<any>();

  onChange() {
    this.dateError = this.validateDates();
    if (!this.dateError) {
      this.filterChanged.emit({
          "tracking_number": this.trackingNumber,
          "status": this.status,
          "text_desc": this.textDesc,
          "start_date" : this.startDate,
          "end_date": this.endDate
        }
      )
    }
  }
}
