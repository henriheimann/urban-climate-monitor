import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import {
  selectEditingMode,
  selectLocationState,
  selectModifiedPosition,
  selectModifiedRotation,
  selectSelectedSensor
} from '../../store/location.selectors';
import {
  revertChanges,
  saveChanges,
  selectSensor,
  setEditingMode,
  setModifiedPosition,
  setModifiedRotation
} from '../../store/location.actions';
import { filter, map, withLatestFrom } from 'rxjs/operators';
import { selectLoggedInUser } from '../../../auth/store/auth.selectors';
import { ActivatedRoute } from '@angular/router';

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

  userHasEditPermission$ = this.store.select(selectLoggedInUser).pipe(
    withLatestFrom(
      this.activatedRoute.params.pipe(
        filter((params) => params?.locationId != undefined),
        map((params) => params.locationId)
      )
    ),
    map(([user, locationId]) => {
      return user != null && (user.role == 'ADMIN' || user.locationsWithPermission.indexOf(locationId) != -1);
    })
  );

  selectedSensorTimestamp$ = this.store.select(selectLocationState).pipe(
    map((state) => {
      if (state.loadedMeasurements?.entries) {
        return new Date(state.loadedMeasurements.entries[state.selectedMeasurementsIndex].timestamp).toLocaleString();
      } else {
        return undefined;
      }
    })
  );

  selectedSensorMeasurements$ = this.store.select(selectLocationState).pipe(
    map((state) => {
      if (state.loadedMeasurements?.entries && state.selectedSensor?.id) {
        return state.loadedMeasurements.entries[state.selectedMeasurementsIndex].measurements[state.selectedSensor.id];
      } else {
        return undefined;
      }
    })
  );

  constructor(private store: Store, private activatedRoute: ActivatedRoute) {}

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
