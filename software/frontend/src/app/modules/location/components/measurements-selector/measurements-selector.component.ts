import { Component } from '@angular/core';
import { Options } from '@angular-slider/ngx-slider';

@Component({
  selector: 'ucm-measurements-selector',
  templateUrl: './measurements-selector.component.html',
  styleUrls: ['./measurements-selector.component.scss']
})
export class MeasurementsSelectorComponent {
  dateRange: Date[] = this.createDateRange();
  value: number = this.dateRange[0].getTime();
  options: Options = {
    stepsArray: this.dateRange.map((date: Date) => {
      return { value: date.getTime() };
    }),
    translate: (value: number): string => {
      return new Date(value).toDateString();
    }
  };

  createDateRange(): Date[] {
    const dates: Date[] = [];
    for (let i: number = 1; i <= 31; i++) {
      dates.push(new Date(2018, 5, i));
    }
    return dates;
  }

  constructor() {}
}
