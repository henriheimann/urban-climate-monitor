import { Injectable } from '@angular/core';
import { EntityCollectionServiceBase, EntityCollectionServiceElementsFactory } from '@ngrx/data';
import { SensorModel } from '../models/sensor.model';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SensorService extends EntityCollectionServiceBase<SensorModel> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory, private httpClient: HttpClient) {
    super('Sensor', serviceElementsFactory);
  }
}
