import { Injectable } from '@angular/core';

import { catchError, filter, map, switchMap, tap, withLatestFrom } from 'rxjs/operators';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { LocationService } from '../../shared/services/location.service';
import { loadMeasurements, loadMeasurementsFailure, loadMeasurementsSuccess, saveChanges } from './location.actions';
import { selectLocationState } from './location.selectors';
import { selectRouteParam } from '../../shared/store/router.selectors';
import { of } from 'rxjs';

@Injectable()
export class LocationEffects {
  saveChanges$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(saveChanges),
        withLatestFrom(
          this.store.select(selectLocationState),
          this.locationService.entityMap$.pipe(
            withLatestFrom(this.store.select(selectRouteParam('locationId'))),
            map(([entityMap, locationId]) => {
              return locationId != undefined ? entityMap[locationId] : undefined;
            })
          )
        ),
        tap(([, state, location]) => {
          if (state.selectedSensor != null && location != null) {
            this.locationService.update({
              id: location.id,
              name: location.name,
              icon: location.icon,
              model3d: location.model3d,
              sensors: location.sensors.map((sensor) => {
                return {
                  id: sensor.id,
                  name: sensor.name,
                  ttnId: sensor.ttnId,
                  locationId: sensor.locationId,
                  position: sensor.id == state.selectedSensor?.id ? state.modifiedPosition || sensor.position : sensor.position,
                  rotation: sensor.id == state.selectedSensor?.id ? state.modifiedRotation || sensor.rotation : sensor.rotation,
                  measurements: sensor.measurements
                };
              })
            });
          }
        })
      ),
    { dispatch: false }
  );

  loadMeasurements$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadMeasurements),
      withLatestFrom(this.store.select(selectRouteParam('locationId')).pipe(filter((locationId) => locationId != null))),
      switchMap(([action, locationId]) => {
        if (locationId) {
          return this.locationService.loadMeasurements(parseInt(locationId), action.measurementsType, action.timeframe).pipe(
            map((measurements) => loadMeasurementsSuccess({ measurements })),
            catchError(() => of(loadMeasurementsFailure()))
          );
        } else {
          return of(loadMeasurementsFailure());
        }
      })
    )
  );

  constructor(private actions$: Actions, private store: Store, private locationService: LocationService) {}
}
