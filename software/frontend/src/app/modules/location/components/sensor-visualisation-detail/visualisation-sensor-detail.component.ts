import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { selectEditingMode, selectModifiedPosition, selectModifiedRotation, selectSelectedSensor } from '../../store/location.selectors';
import { SensorModel } from '../../../shared/models/sensor.model';
import {
  revertChanges,
  saveChanges,
  selectSensor,
  setEditingMode,
  setModifiedPosition,
  setModifiedRotation
} from '../../store/location.actions';

@Component({
  selector: 'ucm-sensor-detail',
  templateUrl: './visualisation-sensor-detail.component.html',
  styleUrls: ['./visualisation-sensor-detail.component.css']
})
export class VisualisationSensorDetailComponent implements OnInit {
  selectedSensor$ = this.store.select(selectSelectedSensor);
  editingMode$ = this.store.select(selectEditingMode);
  modifiedPosition$ = this.store.select(selectModifiedPosition);
  modifiedRotation$ = this.store.select(selectModifiedRotation);

  sensor: SensorModel | null = null;

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.selectedSensor$.subscribe((sensor) => {
      this.sensor = sensor;
    });
  }

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
