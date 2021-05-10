import { Component, Input } from '@angular/core';

@Component({
  selector: 'ucm-measurement',
  templateUrl: './measurement.component.html',
  styleUrls: ['./measurement.component.css']
})
export class MeasurementComponent {
  @Input()
  value: any;

  @Input()
  unit: string | undefined;
}
