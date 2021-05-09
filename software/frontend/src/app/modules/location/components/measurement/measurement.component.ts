import { Component, Input, OnInit } from '@angular/core';

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

  constructor() {}
}
