import { Component, OnInit } from '@angular/core';
import { Options } from '@angular-slider/ngx-slider';
import { MeasurementsTimeframe } from '../../models/measurements-timeframe';
import { MeasurementsType } from '../../models/measurements-type';
import { Store } from '@ngrx/store';
import { selectLocationState } from '../../store/location.selectors';
import { loadMeasurements, selectMeasurementsIndex } from '../../store/location.actions';
import { LocationMeasurementsModel } from '../../../shared/models/location-measurements.model';

@Component({
  selector: 'ucm-measurements-selector',
  templateUrl: './measurements-selector.component.html',
  styleUrls: ['./measurements-selector.component.scss']
})
export class MeasurementsSelectorComponent implements OnInit {
  measurementsTypes = MeasurementsType;
  measurementsTimeframes = MeasurementsTimeframe;

  selectedMeasurementsType: MeasurementsType | undefined;
  selectedMeasurementsTimeframe: MeasurementsTimeframe | undefined;

  loadedMeasurements: LocationMeasurementsModel | undefined;

  sliderSelectedIndex = 0;
  sliderOptions: Options = {
    stepsArray: [
      {
        value: 0
      }
    ],
    translate: (value: number): string => {
      if (this.loadedMeasurements?.entries[value].timestamp) {
        return new Date(this.loadedMeasurements?.entries[value].timestamp).toLocaleString();
      } else {
        return '';
      }
    }
  };

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(
      loadMeasurements({
        measurementsType: MeasurementsType.TEMPERATURE,
        timeframe: MeasurementsTimeframe.LAST_6_HOURS
      })
    );

    this.store.select(selectLocationState).subscribe((locationState) => {
      this.selectedMeasurementsType = locationState.selectedMeasurementsType;
      this.selectedMeasurementsTimeframe = locationState.selectedMeasurementsTimeframe;
      this.sliderSelectedIndex = locationState.selectedMeasurementsIndex;

      this.loadedMeasurements = locationState.loadedMeasurements;
      if (this.loadedMeasurements?.entries) {
        const entries = this.loadedMeasurements.entries;
        const newOptions: Options = Object.assign({}, this.sliderOptions);
        newOptions.stepsArray = entries.map((entry, index) => {
          return {
            value: index
          };
        });
        this.sliderOptions = newOptions;
      }
    });
  }

  onTimeframeSelect(measurementsTimeframe: string): void {
    this.store.dispatch(
      loadMeasurements({
        measurementsType: this.selectedMeasurementsType || MeasurementsType.TEMPERATURE,
        timeframe: (measurementsTimeframe as unknown) as MeasurementsTimeframe
      })
    );
  }

  onTypeSelect(measurementsType: string): void {
    this.store.dispatch(
      loadMeasurements({
        measurementsType: (measurementsType as unknown) as MeasurementsType,
        timeframe: this.selectedMeasurementsTimeframe || MeasurementsTimeframe.LAST_6_HOURS
      })
    );
  }

  onSliderChange(): void {
    this.store.dispatch(
      selectMeasurementsIndex({
        index: this.sliderSelectedIndex
      })
    );
  }
}
