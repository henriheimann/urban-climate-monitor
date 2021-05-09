import { createReducer, on } from '@ngrx/store';
import * as AlertActions from './alert.actions';
import { Alert } from '../models/alert.model';

export const alertFeatureKey = 'alert';

export interface AlertState {
  alerts: Alert[];
}

export const initialState: AlertState = {
  alerts: []
};

export const reducer = createReducer(
  initialState,

  on(AlertActions.dispatchAlert, (state, action) => ({
    ...state,
    alerts: [...state.alerts, action.alert]
  })),

  on(AlertActions.clearAlertsForDestination, (state, action) => ({
    ...state,
    alerts: [...state.alerts.filter((alert) => alert.destination !== action.destination)]
  }))
);
