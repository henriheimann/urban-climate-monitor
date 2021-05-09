import { Component, EventEmitter, Input, Output } from '@angular/core';
import { LocationSensor } from '../../../shared/models/location-sensor.model';
import { LocationService } from '../../services/location.service';
import { Observable } from 'rxjs';
import { SensorLatestMeasurements } from '../../models/sensor-measurements.model';

@Component({
  selector: 'ucm-sensor-popup',
  templateUrl: './sensor-popup.component.html',
  styleUrls: ['./sensor-popup.component.css']
})
export class SensorPopupComponent {
  @Input()
  set locationSensor(locationSensor: LocationSensor) {
    this.sensor = locationSensor;
    if (this.sensor) {
      this.sensorLatestMeasurements$ = this.locationService.getCurrentRouteLocationSensorLatestMeasurements(
        locationSensor.id
      );
    }
  }

  editingMode = 'none';

  @Input() editedPosition: number[] | undefined;

  @Output() editedPositionChange = new EventEmitter<number[]>();

  @Input() editedRotation: number[] | undefined;

  @Output() editedRotationChange = new EventEmitter<number[]>();

  @Output() closePopup = new EventEmitter<void>();

  @Output() editingModeChange = new EventEmitter<string>();

  @Output() saveChanges = new EventEmitter<void>();

  @Output() revertChanges = new EventEmitter<void>();

  sensor!: LocationSensor;

  sensorLatestMeasurements$: Observable<SensorLatestMeasurements> | undefined;

  constructor(private locationService: LocationService) {}

  onEditingChange($event: boolean, mode: string): void {
    if ($event) {
      this.editingMode = mode;
    } else {
      this.editingMode = 'none';
    }
    this.editingModeChange.emit(this.editingMode);
  }
}
