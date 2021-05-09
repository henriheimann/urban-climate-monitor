import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { SensorLatestMeasurements } from '../../models/sensor-measurements.model';
import { LocationService } from '../../services/location.service';

@Component({
  selector: 'ucm-sensor-list',
  templateUrl: './sensor-list.component.html',
  styleUrls: ['./sensor-list.component.css']
})
export class SensorListComponent implements OnInit {
  locationSensorsLatestMeasurements$: Observable<SensorLatestMeasurements[]> | undefined;

  constructor(private locationService: LocationService) {}

  ngOnInit(): void {
    this.locationSensorsLatestMeasurements$ = this.locationService.getCurrentRouteLocationSensorsLatestMeasurements();
  }
}
