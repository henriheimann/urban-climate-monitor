import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { selectSelectedMeasurementsInfo } from '../../store/location.selectors';
import { MeasurementsType } from '../../models/measurements-type';
import { MeasurementColorsService } from '../../services/measurement-colors.service';

@Component({
  selector: 'ucm-visualisation-legend',
  templateUrl: './visualisation-legend.component.html',
  styleUrls: ['./visualisation-legend.component.css']
})
export class VisualisationLegendComponent {
  min = 0;
  midLower = 0;
  midUpper = 0;
  max = 0;
  unit = '';
  valid = false;

  constructor(private store: Store, private measurementColorsService: MeasurementColorsService) {
    store.select(selectSelectedMeasurementsInfo).subscribe((limits) => {
      if (limits === undefined || limits.min === undefined || limits.max === undefined) {
        this.valid = false;
      } else {
        this.valid = true;
        this.min = limits.min;
        this.max = limits.max;
        this.midLower = this.min + (this.max - this.min) / 3;
        this.midUpper = this.min + ((this.max - this.min) / 3) * 2;
        if (limits.type == MeasurementsType.BRIGHTNESS_CURRENT) {
          this.unit = '';
          this.midLower = this.measurementColorsService.mapValueToCurveInverse(this.midLower, this.min, this.max);
          this.midUpper = this.measurementColorsService.mapValueToCurveInverse(this.midUpper, this.min, this.max);
        } else if (limits.type == MeasurementsType.HUMIDITY) {
          this.unit = 'measurement.relative_humidity';
        } else {
          this.unit = 'measurement.degrees_c';
        }
      }
    });
  }
}
