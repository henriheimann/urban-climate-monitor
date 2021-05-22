import { createReducer, on } from '@ngrx/store';
import * as LocationActions from './location.actions';
import { SensorModel } from '../../shared/models/sensor.model';
import { MeasurementsTimeframe } from '../models/measurements-timeframe';
import { MeasurementsType } from '../models/measurements-type';
import { LocationMeasurementsModel } from '../../shared/models/location-measurements.model';

export const locationFeatureKey = 'location';

export interface LocationState {
  // Sensor selection and editing
  selectedSensor: SensorModel | null;
  modifiedPosition: number[] | null;
  modifiedRotation: number[] | null;
  editingMode: 'translate' | 'rotate' | 'none';

  // Measurements visualisation
  selectedMeasurementsType: MeasurementsType;
  selectedMeasurementsTimeframe: MeasurementsTimeframe;
  selectedMeasurementsIndex: number;

  loadingMeasurements: boolean;
  loadedMeasurements: LocationMeasurementsModel | undefined;
}

export const initialState: LocationState = {
  selectedSensor: null,
  modifiedPosition: null,
  modifiedRotation: null,
  editingMode: 'none',

  selectedMeasurementsIndex: 0,
  selectedMeasurementsType: MeasurementsType.TEMPERATURE,
  selectedMeasurementsTimeframe: MeasurementsTimeframe.LAST_6_HOURS,

  loadingMeasurements: false,
  loadedMeasurements: undefined
};

export const reducer = createReducer(
  initialState,

  on(LocationActions.selectSensor, (state, action) => {
    return {
      ...state,
      selectedSensor: action.sensor,
      modifiedPosition: action?.sensor?.position ? action.sensor.position : null,
      modifiedRotation: action?.sensor?.rotation ? action.sensor.rotation : null,
      editingMode: 'none'
    };
  }),

  on(LocationActions.setEditingMode, (state, action) => {
    return {
      ...state,
      editingMode: action.editingMode
    };
  }),

  on(LocationActions.saveChanges, (state) => {
    let selectedSensor: SensorModel | null;
    if (state.selectedSensor) {
      selectedSensor = {
        id: state.selectedSensor.id,
        name: state.selectedSensor.name,
        ttnId: state.selectedSensor.ttnId,
        locationId: state.selectedSensor.locationId,
        position: state.modifiedPosition || [0, 0, 0],
        rotation: state.modifiedRotation || [0, 0, 0],
        measurements: state.selectedSensor.measurements
      };
    } else {
      selectedSensor = null;
    }
    return {
      ...state,
      selectedSensor
    };
  }),

  on(LocationActions.revertChanges, (state) => {
    return {
      ...state,
      modifiedPosition: state?.selectedSensor?.position ? state.selectedSensor.position : null,
      modifiedRotation: state?.selectedSensor?.rotation ? state.selectedSensor.rotation : null
    };
  }),

  on(LocationActions.setModifiedPosition, (state, action) => {
    return {
      ...state,
      modifiedPosition: action.position
    };
  }),

  on(LocationActions.setModifiedRotation, (state, action) => {
    return {
      ...state,
      modifiedRotation: action.rotation
    };
  }),

  on(LocationActions.loadMeasurements, (state, action) => {
    return {
      ...state,
      selectedMeasurementsType: action.measurementsType,
      selectedMeasurementsTimeframe: action.timeframe,
      loadingMeasurements: true
    };
  }),

  on(LocationActions.loadMeasurementsSuccess, (state, action) => {
    return {
      ...state,
      loadingMeasurements: false,
      loadedMeasurements: action.measurements,
      selectedMeasurementsIndex: action.measurements.entries.length - 1
    };
  }),

  on(LocationActions.loadMeasurementsFailure, (state) => {
    return {
      ...state,
      loadingMeasurements: false
    };
  }),

  on(LocationActions.selectMeasurementsIndex, (state, action) => {
    return {
      ...state,
      selectedMeasurementsIndex: action.index
    };
  })
);
