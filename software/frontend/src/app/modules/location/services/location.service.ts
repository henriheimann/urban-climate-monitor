import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SensorLatestMeasurements } from '../models/sensor-measurements.model';
import { environment } from '../../../../environments/environment';
import { Router } from '@angular/router';
import { EntityCollectionService, EntityCollectionServiceFactory } from '@ngrx/data';
import { Location } from '../../shared/models/location.model';
import { LocationSensor } from '../../shared/models/location-sensor.model';

@Injectable({
  providedIn: 'root'
})
export class LocationService {
  entityCollectionService: EntityCollectionService<Location>;

  constructor(
    private httpClient: HttpClient,
    private router: Router,
    EntityCollectionServiceFactoryClass: EntityCollectionServiceFactory
  ) {
    this.entityCollectionService = EntityCollectionServiceFactoryClass.create<Location>('Location');
  }

  getCurrentRouteLocationSensorsLatestMeasurements(): Observable<SensorLatestMeasurements[]> {
    return this.httpClient.get<SensorLatestMeasurements[]>(
      `${environment.backendUrl}/location/${this.getCurrentRouteLocationId()}/sensors/latest-measurements`
    );
  }

  getCurrentRouteLocationSensorLatestMeasurements(sensorId: number): Observable<SensorLatestMeasurements> {
    return this.httpClient.get<SensorLatestMeasurements>(
      `${environment.backendUrl}/location/${this.getCurrentRouteLocationId()}/sensor/${sensorId}/latest-measurements`
    );
  }

  getCurrentRouteLocationSensors(): Observable<LocationSensor[]> {
    return this.httpClient.get<LocationSensor[]>(
      `${environment.backendUrl}/location/${this.getCurrentRouteLocationId()}/sensors`
    );
  }

  updateCurrentRouteLocationSensor(locationSensor: LocationSensor): Observable<LocationSensor> {
    return this.httpClient.put<LocationSensor>(
      `${environment.backendUrl}/location/${this.getCurrentRouteLocationId()}/sensor/${locationSensor.id}`,
      locationSensor
    );
  }

  getCurrentRouteLocation(): Observable<Location> {
    return this.entityCollectionService.getByKey(this.getCurrentRouteLocationId());
  }

  getCurrentRouteLocationId(): number {
    return parseInt(this.router.url.split('/')[2], 10);
  }
}
