import {Component, OnInit} from '@angular/core';
import {MeasurementsService} from '../../services/measurements.service';
import {Router} from '@angular/router';
import {Observable} from 'rxjs';
import {SensorLatestMeasurements} from '../../models/sensor-measurements.model';

@Component({
  selector: 'ucm-sensor-list',
  templateUrl: './sensor-list.component.html',
  styleUrls: ['./sensor-list.component.css']
})
export class SensorListComponent implements OnInit {

  locationSensorsLatestMeasurements$: Observable<SensorLatestMeasurements[]> | undefined;

  constructor(private measurementsService: MeasurementsService, private router: Router) { }

  ngOnInit(): void {
    this.locationSensorsLatestMeasurements$ = this.measurementsService.getLocationSensorsLatestMeasurements(this.getRouteLocationId());
  }

  getRouteLocationId(): number {
    return parseInt(this.router.url.split('/')[2], 10);
  }
}
