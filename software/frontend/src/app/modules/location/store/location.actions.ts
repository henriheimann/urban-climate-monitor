import { createAction, props } from '@ngrx/store';
import { SensorModel } from '../../shared/models/sensor.model';

export const selectSensor = createAction('[Location] Select Sensor', props<{ sensor: SensorModel | null }>());

export const setEditingMode = createAction('[Location] Set Editing Mode', props<{ editingMode: 'translate' | 'rotate' | 'none' }>());

export const revertChanges = createAction('[Location] Revert Changes');

export const saveChanges = createAction('[Location] Save Changes');

export const setModifiedPosition = createAction('[Location] Set Modified Position', props<{ position: number[] }>());

export const setModifiedRotation = createAction('[Location] Set Modified Rotation', props<{ rotation: number[] }>());
