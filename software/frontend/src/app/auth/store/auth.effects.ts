import {Injectable} from '@angular/core';
import {Actions, createEffect, Effect, ofType} from '@ngrx/effects';

import {catchError, map, switchMap, tap} from 'rxjs/operators';
import {EMPTY, of} from 'rxjs';

import * as AuthActions from './auth.actions';
import {AuthService} from '../services/auth.service';
import {loginUserFailure, loginUserSuccess} from './auth.actions';

@Injectable()
export class AuthEffects {

  loginUser$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.loginUser),
      switchMap(action => {
        return this.authService.loginUser(action.username, action.password).pipe(
          map(tokenAndUser => {
            console.log(tokenAndUser);
            return loginUserSuccess(tokenAndUser);
          }),
          catchError(error => of(loginUserFailure({ error })))
        );
      })
    );
  });

  /*loginUserSuccess$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.loginUserSuccess),
      tap(() => console.log('success'))
    );
  });

  loginUserFailure$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.loginUserFailure),
      tap(() => console.log('failure'))
    );
  });*/

  constructor(private actions$: Actions, private authService: AuthService) {
  }
}
