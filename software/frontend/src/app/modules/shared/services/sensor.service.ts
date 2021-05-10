import { Injectable } from '@angular/core';
import { EntityCollectionServiceBase, EntityCollectionServiceElementsFactory } from '@ngrx/data';
import { SensorModel } from '../models/sensor.model';

@Injectable({
  providedIn: 'root'
})
export class SensorService extends EntityCollectionServiceBase<SensorModel> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super('Sensor', serviceElementsFactory);
  }
}
