import { createAction, props } from '@ngrx/store';
import { SensorModel } from '../../shared/models/sensor.model';
import { MeasurementsTimeframe } from '../models/measurements-timeframe';
import { MeasurementsType } from '../models/measurements-type';
import { LocationMeasurementsModel } from '../../shared/models/location-measurements.model';

export const selectSensor = createAction('[Location] Select Sensor', props<{ sensor: SensorModel | null }>());

export const setEditingMode = createAction('[Location] Set Editing Mode', props<{ editingMode: 'translate' | 'rotate' | 'none' }>());

export const revertChanges = createAction('[Location] Revert Changes');

export const saveChanges = createAction('[Location] Save Changes');

export const setModifiedPosition = createAction('[Location] Set Modified Position', props<{ position: number[] }>());

export const setModifiedRotation = createAction('[Location] Set Modified Rotation', props<{ rotation: number[] }>());

export const loadMeasurements = createAction(
  '[Location] Select Measurements',
  props<{
    measurementsType: MeasurementsType;
    timeframe: MeasurementsTimeframe;
  }>()
);

export const loadMeasurementsSuccess = createAction(
  '[Location] Load Measurements Success',
  props<{ measurements: LocationMeasurementsModel }>()
);

export const loadMeasurementsFailure = createAction('[Location] Load Measurements Failure');

export const selectMeasurementsIndex = createAction('[Location] Select Measurements Index', props<{ index: number }>());
