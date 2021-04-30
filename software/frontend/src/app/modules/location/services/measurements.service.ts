import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../../environments/environment';
import {SensorLatestMeasurements} from '../models/sensor-measurements.model';

@Injectable({
  providedIn: 'root'
})
export class MeasurementsService {

  constructor(private httpClient: HttpClient) {

  }

  getLocationSensorsLatestMeasurements(locationId: number): Observable<SensorLatestMeasurements[]> {
    return this.httpClient.get<SensorLatestMeasurements[]>(`${environment.backendUrl}/location/${locationId}/sensors/latest-measurements`);
  }
}
