import { createReducer } from '@ngrx/store';

export const locationFeatureKey = 'location';

export interface LocationState {
  location: string;
}

export const initialState: LocationState = {
  location: ''
};

export const reducer = createReducer(initialState);
