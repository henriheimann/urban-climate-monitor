import { createFeatureSelector, createSelector } from '@ngrx/store';
import * as fromLocation from './location.reducer';
import { LocationState } from './location.reducer';

export const selectLocationState = createFeatureSelector<fromLocation.LocationState>(fromLocation.locationFeatureKey);

export const selectSelectedSensor = createSelector(selectLocationState, (state: LocationState) => state.selectedSensor);

export const selectEditingMode = createSelector(selectLocationState, (state: LocationState) => state.editingMode);

export const selectModifiedPosition = createSelector(selectLocationState, (state: LocationState) => state.modifiedPosition);

export const selectModifiedRotation = createSelector(selectLocationState, (state: LocationState) => state.modifiedRotation);

export const selectLoadingMeasurements = createSelector(selectLocationState, (state: LocationState) => state.loadingMeasurements);

export const selectLoadedMeasurements = createSelector(selectLocationState, (state: LocationState) => state.loadedMeasurements);

export const selectSelectedMeasurementsInfo = createSelector(selectLocationState, (state: LocationState) => {
  if (state.loadedMeasurementsMin != undefined && state.loadedMeasurementsMax != undefined) {
    return {
      min: state.loadedMeasurementsMin[state.selectedMeasurementsType],
      max: state.loadedMeasurementsMax[state.selectedMeasurementsType],
      type: state.selectedMeasurementsType
    };
  }
  return undefined;
});
