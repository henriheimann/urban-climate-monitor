import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import {
  selectEditingMode,
  selectLocationState,
  selectModifiedPosition,
  selectModifiedRotation,
  selectSelectedSensor
} from '../../store/location.selectors';
import { SensorModel } from '../../../shared/models/sensor.model';
import {
  revertChanges,
  saveChanges,
  selectSensor,
  setEditingMode,
  setModifiedPosition,
  setModifiedRotation
} from '../../store/location.actions';
import { SensorService } from '../../../shared/services/sensor.service';
import { map, switchMap } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'ucm-sensor-detail',
  templateUrl: './visualisation-sensor-detail.component.html',
  styleUrls: ['./visualisation-sensor-detail.component.css']
})
export class VisualisationSensorDetailComponent {
  editingMode$ = this.store.select(selectEditingMode);
  modifiedPosition$ = this.store.select(selectModifiedPosition);
  modifiedRotation$ = this.store.select(selectModifiedRotation);

  selectedSensor$ = this.store.select(selectSelectedSensor);

  selectedSensorMeasurements$ = this.store.select(selectLocationState).pipe(
    map((locationState) => {
      if (locationState.selectedSensor?.id && locationState.loadedMeasurements?.entries) {
        const selectedEntry = locationState.loadedMeasurements.entries[locationState.selectedMeasurementsIndex];
        if (selectedEntry) {
          return {
            id: locationState.selectedSensor.id,
            timestamp: selectedEntry.timestamp
          };
        }
      }
      return undefined;
    }),
    switchMap((info) => {
      if (info) {
        return this.sensorService.loadMeasurements(info.id, info.timestamp);
      } else {
        return of(undefined);
      }
    })
  );

  constructor(private store: Store, private sensorService: SensorService) {}

  onCloseClicked(): void {
    this.store.dispatch(selectSensor({ sensor: null }));
  }

  onEditingChange($event: boolean, mode: 'translate' | 'rotate'): void {
    if (!$event) {
      this.store.dispatch(setEditingMode({ editingMode: 'none' }));
    } else {
      this.store.dispatch(setEditingMode({ editingMode: mode }));
    }
  }

  onSaveChanges(): void {
    this.store.dispatch(saveChanges());
  }

  onRevertChanges(): void {
    this.store.dispatch(revertChanges());
  }

  onPositionChange($event: number[]): void {
    this.store.dispatch(setModifiedPosition({ position: $event }));
  }

  onRotationChange($event: number[]): void {
    this.store.dispatch(setModifiedRotation({ rotation: $event }));
  }
}
