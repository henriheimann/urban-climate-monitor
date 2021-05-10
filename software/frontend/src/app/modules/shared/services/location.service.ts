import { Injectable } from '@angular/core';
import { EntityCollectionServiceBase, EntityCollectionServiceElementsFactory } from '@ngrx/data';
import { LocationModel } from '../models/location.model';

@Injectable({
  providedIn: 'root'
})
export class LocationService extends EntityCollectionServiceBase<LocationModel> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super('Location', serviceElementsFactory);
  }
}
