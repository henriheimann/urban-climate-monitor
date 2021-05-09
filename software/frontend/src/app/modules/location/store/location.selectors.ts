import { createFeatureSelector } from '@ngrx/store';
import * as fromLocation from './location.reducer';

export const selectLocationState = createFeatureSelector<fromLocation.LocationState>(fromLocation.locationFeatureKey);
