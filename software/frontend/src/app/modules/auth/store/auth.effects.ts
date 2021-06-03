import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';

import { catchError, map, switchMap, tap, withLatestFrom } from 'rxjs/operators';
import { of } from 'rxjs';

import * as AuthActions from './auth.actions';
import {
  loadUserFromLocalStorageFailure,
  loadUserFromLocalStorageSuccess,
  loginUserFailure,
  loginUserGetTokensSuccess,
  loginUserGetUserSuccess,
  logoutUserSuccess,
  refreshUserTokenFailure,
  refreshUserTokenSuccess
} from './auth.actions';
import { AuthService } from '../services/auth.service';
import { UserModel } from '../../shared/models/user.model';
import { dispatchAlert } from '../../alert/store/alert.actions';
import { Store } from '@ngrx/store';
import { selectAuthState } from './auth.selectors';
import { Router } from '@angular/router';
import { Alert } from '../../alert/models/alert.model';
import { UserService } from '../../shared/services/user.service';

@Injectable()
export class AuthEffects {
  loginUser$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.loginUser),
      switchMap((action) =>
        this.authService.getTokens(action.username, action.password).pipe(
          map((tokens) =>
            loginUserGetTokensSuccess({
              username: action.username,
              token: tokens.token,
              refreshToken: tokens.refreshToken
            })
          ),
          catchError((error) => of(loginUserFailure({ error })))
        )
      )
    )
  );

  loginUserGetTokensSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.loginUserGetTokensSuccess),
      switchMap((action) =>
        this.userService.getByKey(action.username).pipe(
          map((user) => loginUserGetUserSuccess({ user })),
          catchError((error) => of(loginUserFailure({ error })))
        )
      )
    )
  );

  loginUserFailure$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.loginUserFailure),
      map(() => dispatchAlert({ alert: new Alert('alert.invalid_credentials', 'login') }))
    )
  );

  loginUserGetUserSuccessSaveLocalStorage$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.loginUserGetUserSuccess),
        withLatestFrom(this.store.select(selectAuthState)),
        tap(([, state]) => {
          if (state.user != null && state.token != null && state.refreshToken != null) {
            this.authService.saveLocalStorage(state.user, state.token, state.refreshToken);
          }
        })
      ),
    { dispatch: false }
  );

  loginUserFailureClearLocalStorage$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.loginUserFailure),
        tap(() => this.authService.clearLocalStorage())
      ),
    { dispatch: false }
  );

  loadUserFromLocalStorage$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.loadUserFromLocalStorage),
      map(() => {
        const fromLocalStorage = this.authService.loadLocalStorage();
        if (fromLocalStorage != null) {
          return loadUserFromLocalStorageSuccess(fromLocalStorage);
        }
        return loadUserFromLocalStorageFailure();
      })
    )
  );

  refreshUserToken$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.refreshUserToken),
      withLatestFrom(this.store.select(selectAuthState)),
      switchMap(([, state]) => {
        if (state.refreshToken != null) {
          return this.authService.getTokensViaRefresh(state.refreshToken).pipe(
            map((tokens) =>
              refreshUserTokenSuccess({
                token: tokens.token,
                refreshToken: tokens.refreshToken
              })
            ),
            catchError((error) => of(refreshUserTokenFailure({ error })))
          );
        }
        return of(refreshUserTokenFailure({ error: null }));
      })
    )
  );

  logoutUser$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.logoutUser),
      withLatestFrom(this.store.select(selectAuthState)),
      switchMap(([, state]) => {
        if (state.token != null) {
          return this.authService.revokeToken(state.token).pipe(
            map(() => {
              this.authService.clearLocalStorage();
              return logoutUserSuccess();
            }),
            catchError(() => {
              this.authService.clearLocalStorage();
              return of(logoutUserSuccess());
            })
          );
        }
        return of(logoutUserSuccess());
      })
    )
  );

  logoutUserSuccess$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.logoutUserSuccess),
        tap(() => {
          this.router.navigate(['/']).then();
        })
      ),
    { dispatch: false }
  );

  refreshUserTokenSuccess$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.refreshUserTokenSuccess),
        withLatestFrom(this.store.select(selectAuthState)),
        tap(([action, state]) => {
          if (state.user != null && action.token != null && action.refreshToken != null) {
            this.authService.saveLocalStorage(state.user, action.token, action.refreshToken);
          }
        })
      ),
    { dispatch: false }
  );

  refreshUserTokenFailure$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.refreshUserTokenFailure),
        tap(() => this.authService.clearLocalStorage())
      ),
    { dispatch: false }
  );

  constructor(
    private actions$: Actions,
    private store: Store,
    private authService: AuthService,
    private router: Router,
    private userService: UserService
  ) {}
}
