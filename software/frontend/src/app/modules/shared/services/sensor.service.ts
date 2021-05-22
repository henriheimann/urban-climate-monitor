import { Injectable } from '@angular/core';
import { EntityCollectionServiceBase, EntityCollectionServiceElementsFactory } from '@ngrx/data';
import { SensorModel } from '../models/sensor.model';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { SensorMeasurementsModel } from '../models/sensor-measurements.model';

@Injectable({
  providedIn: 'root'
})
export class SensorService extends EntityCollectionServiceBase<SensorModel> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory, private httpClient: HttpClient) {
    super('Sensor', serviceElementsFactory);
  }

  loadMeasurements(sensorId: number, timestamp: string): Observable<SensorMeasurementsModel> {
    return this.httpClient.post<SensorMeasurementsModel>(`${environment.backendUrl}/sensor/${sensorId}/measurements`, {
      timestamp
    });
  }
}
