import { Injectable } from '@angular/core';
import { EntityCollectionServiceBase, EntityCollectionServiceElementsFactory } from '@ngrx/data';
import { UserModel } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService extends EntityCollectionServiceBase<UserModel> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super('User', serviceElementsFactory);
  }
}
