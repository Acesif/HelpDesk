import {
  Component,
  ElementRef,
  EventEmitter,
  Output,
  AfterViewInit,
} from '@angular/core';
import flatpickr from 'flatpickr';
import monthSelectPlugin from 'flatpickr/dist/plugins/monthSelect';

@Component({
  selector: 'app-filterform',
  templateUrl: './filter-form.component.html',
  styleUrl: './filter-form.component.scss'
})
export class FilterFormComponent implements AfterViewInit {
  startDate: string;
  endDate: string;
  trackingNumber: number;
  textDesc: string;
  status: string;
  maxDate: string;
  dateError: boolean = false;

  constructor(private elRef: ElementRef) {
    this.status = "";
    this.textDesc = "";
  }

  ngOnInit() {
    const now = new Date();
    const oneMonthAgo = new Date(now);
    oneMonthAgo.setMonth(oneMonthAgo.getMonth() - 1);

    this.startDate = oneMonthAgo.toISOString().slice(0, 7);
    this.endDate = now.toISOString().slice(0, 7);
    this.maxDate = now.toISOString().slice(0, 7);
  }

  ngAfterViewInit() {
    const startDateEl = this.elRef.nativeElement.querySelector('#startDate');
    const endDateEl = this.elRef.nativeElement.querySelector('#endDate');

    flatpickr(startDateEl, {
      plugins: [monthSelectPlugin({ shorthand: true, dateFormat: 'Y-m' })],
      defaultDate: this.startDate,
      maxDate: this.maxDate,
      onChange: (_, dateStr: string) => {
        this.startDate = dateStr;
        this.onChange();
      }
    });

    flatpickr(endDateEl, {
      plugins: [monthSelectPlugin({ shorthand: true, dateFormat: 'Y-m' })],
      defaultDate: this.endDate,
      maxDate: this.maxDate,
      onChange: (_, dateStr: string) => {
        this.endDate = dateStr;
        this.onChange();
      }
    });
  }

  validateDates(): boolean {
    return this.startDate > this.endDate;
  }

  @Output() filterChanged = new EventEmitter<any>();

  onChange() {
    this.dateError = this.validateDates();
    if (!this.dateError) {
      this.filterChanged.emit({
        tracking_number: this.trackingNumber,
        status: this.status,
        text_desc: this.textDesc,
        start_date: this.startDate,
        end_date: this.endDate
      });
    }
  }
}
