import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { selectSelectedMeasurementsInfo } from '../../store/location.selectors';
import { MeasurementsType } from '../../models/measurements-type';

@Component({
  selector: 'ucm-visualisation-legend',
  templateUrl: './visualisation-legend.component.html',
  styleUrls: ['./visualisation-legend.component.css']
})
export class VisualisationLegendComponent {
  min = 0;
  max = 0;
  unit = '';

  constructor(private store: Store) {
    store.select(selectSelectedMeasurementsInfo).subscribe((limits) => {
      this.min = limits?.min || 0;
      this.max = limits?.max || 0;
      if (limits?.type == MeasurementsType.BRIGHTNESS_CURRENT) {
        this.unit = '';
      } else if (limits?.type == MeasurementsType.HUMIDITY) {
        this.unit = 'measurement.relative_humidity';
      } else {
        this.unit = 'measurement.degrees_c';
      }
    });
  }
}
