import { createAction, props } from '@ngrx/store';
import { Alert } from '../models/alert.model';

export const dispatchAlert = createAction('[Alert] Dispatch Alert', props<{ alert: Alert }>());

export const clearAlertsForDestination = createAction('[Alert] Clear Alerts For Destination', props<{ destination: string }>());
