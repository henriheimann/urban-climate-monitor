import { Component, OnInit } from '@angular/core';
import { LocationModel } from '../../modules/shared/models/location.model';
import { SafeResourceUrl } from '@angular/platform-browser';
import { environment } from '../../../environments/environment';
import { LocationService } from '../../modules/shared/services/location.service';

@Component({
  selector: 'ucm-index',
  templateUrl: './front-page.component.html',
  styleUrls: ['./front-page.component.scss']
})
export class FrontPageComponent implements OnInit {
  loading$ = this.locationService.loading$;
  locations$ = this.locationService.entities$;

  constructor(private locationService: LocationService) {}

  ngOnInit(): void {
    this.locationService.getAll();
  }

  getLocationIcon(location: LocationModel): SafeResourceUrl {
    return environment.backendUrl + location.icon.url;
  }

  getSensorsOnlineString(location: LocationModel): string {
    let sensorsOnline = 0;
    let sensorsOffline = 0;

    for (const sensor of location.sensors) {
      if (sensor.measurements == undefined) {
        sensorsOffline++;
      } else {
        const date = new Date(sensor.measurements['time']);
        const minuteDifference = Math.floor((new Date().getTime() - date.getTime()) / (1000 * 60));
        if (minuteDifference <= 10) {
          sensorsOnline++;
        } else {
          sensorsOffline++;
        }
      }
    }

    return sensorsOnline + '/' + (sensorsOnline + sensorsOffline);
  }

  getAverageMeasurement(location: LocationModel, measurementType: string): number | null {
    let measurementsTotal = 0;
    let measurementsCount = 0;

    for (const sensor of location.sensors) {
      if (sensor.measurements != undefined) {
        const measurement = sensor.measurements[measurementType];
        if (measurement != undefined) {
          measurementsTotal += measurement;
          measurementsCount++;
        }
      }
    }

    if (measurementsCount == 0) {
      return null;
    } else {
      return measurementsTotal / measurementsCount;
    }
  }
}
