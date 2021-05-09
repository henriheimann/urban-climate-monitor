import { createFeatureSelector, createSelector } from '@ngrx/store';
import * as fromAlert from './alert.reducer';

export const selectAlertState = createFeatureSelector<fromAlert.AlertState>(fromAlert.alertFeatureKey);

export const selectAlertsForDestination = createSelector(
  selectAlertState,
  (state: fromAlert.AlertState, props: { destination: string }) =>
    state.alerts.filter((alert) => alert.destination === props.destination)
);
