import { Injectable } from '@angular/core';

import { tap } from 'rxjs/operators';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { ROUTER_NAVIGATED } from '@ngrx/router-store';

@Injectable()
export class LocationEffects {
  routerNavigated$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(ROUTER_NAVIGATED),
        tap((action) => {
          console.log(action);
        })
      ),
    { dispatch: false }
  );

  constructor(private actions$: Actions) {}
}
