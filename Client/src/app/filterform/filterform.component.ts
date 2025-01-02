import {AfterViewInit, Component, ViewChild} from '@angular/core';

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
  maxDate: string;
  dateError: boolean = false;

  constructor() {
    this.status = "";
    this.textdesc = "";
  }

  getFilterData(){
    return "seomthing"
  }

  ngOnInit() {
    const date = new Date();
    date.setMonth(date.getMonth() - 6);
    this.startDate = date.toISOString().slice(0, 7);
    this.endDate = new Date().toISOString().slice(0, 7);
    this.maxDate = new Date().toISOString().slice(0, 7);
  }
  onSubmit(formData: any) {
    console.log(formData);
  }

  validateDates(): void {
    if (this.startDate && this.endDate) {
      const dateError: boolean = this.startDate > this.endDate;
      if (dateError) {
        this.dateError = true;
        setTimeout(() => {
          this.dateError = false;
          this.endDate = new Date().toISOString().slice(0, 7);
        }, 2000);
      } else {
        this.dateError = false;
      }
    } else {
      this.dateError = false;
    }
  }


  statusChange() {
    console.log(this.status);
  }
}
