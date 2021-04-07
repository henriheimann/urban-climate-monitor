import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';

import {catchError, map, mergeMap, switchMap, tap, withLatestFrom} from 'rxjs/operators';
import {of} from 'rxjs';

import * as AuthActions from './auth.actions';
import {
  loadUserFromLocalStorageFailure,
  loadUserFromLocalStorageSuccess,
  loginUserFailure,
  loginUserGetTokensSuccess,
  loginUserGetUserSuccess, logoutUserSuccess, refreshUserTokenFailure, refreshUserTokenSuccess
} from './auth.actions';
import {AuthService} from '../services/auth.service';
import {EntityCollectionService, EntityCollectionServiceFactory} from '@ngrx/data';
import {User} from '../../user/models/user.model';
import {dispatchAlert} from '../../alert/store/alert.actions';
import {Store} from '@ngrx/store';
import {selectAuthState, selectLoggedInUser} from './auth.selectors';

@Injectable()
export class AuthEffects {

  loginUser$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.loginUser),
      switchMap(action => {
        return this.authService.getTokens(action.username, action.password).pipe(
          map(tokens => {
            return loginUserGetTokensSuccess({
              username: action.username,
              token: tokens.token,
              refreshToken: tokens.refreshToken
            });
          }),
          catchError(error => of(loginUserFailure({ error })))
        );
      })
    );
  });

  loginUserGetTokensSuccess$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.loginUserGetTokensSuccess),
      switchMap(action => {
        return this.userService.getByKey(action.username).pipe(
          map(user => {
            return loginUserGetUserSuccess({user});
          }),
          catchError(error => of(loginUserFailure({ error })))
        );
      })
    );
  });

  loginUserFailure$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.loginUserFailure),
      map(action => dispatchAlert({alert: {destination: 'login', message: action.error.message}}))
    );
  });

  loginUserGetUserSuccessSaveLocalStorage$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.loginUserGetUserSuccess),
      withLatestFrom(this.store.select(selectAuthState)),
      tap(([, state]) => {
        if (state.user != null && state.token != null && state.refreshToken != null) {
          this.authService.saveLocalStorage(state.user, state.token, state.refreshToken);
        }
      })
    );
  }, { dispatch: false });

  loginUserFailureClearLocalStorage$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.loginUserFailure),
      tap(() => this.authService.clearLocalStorage())
    );
  }, { dispatch: false });

  loadUserFromLocalStorage$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.loadUserFromLocalStorage),
      map(() => {
        const fromLocalStorage = this.authService.loadLocalStorage();
        if (fromLocalStorage != null) {
          return loadUserFromLocalStorageSuccess(fromLocalStorage);
        } else {
          return loadUserFromLocalStorageFailure();
        }
      })
    );
  });

  refreshUserToken$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.refreshUserToken),
      withLatestFrom(this.store.select(selectAuthState)),
      switchMap(([, state]) => {
        if (state.refreshToken != null) {
          return this.authService.getTokensViaRefresh(state.refreshToken).pipe(
            map(tokens => {
              return refreshUserTokenSuccess({
                token: tokens.token,
                refreshToken: tokens.refreshToken
              });
            }),
            catchError(error => of(refreshUserTokenFailure({error})))
          );
        } else {
          return of(refreshUserTokenFailure({error: null}));
        }
      })
    );
  });

  logoutUser$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.logoutUser),
      withLatestFrom(this.store.select(selectAuthState)),
      switchMap(([, state]) => {
        if (state.refreshToken != null) {
          return this.authService.revokeRefreshToken(state.refreshToken).pipe(
            map(() => {
              this.authService.clearLocalStorage();
              return logoutUserSuccess();
            }),
            catchError(() => {
              this.authService.clearLocalStorage();
              return of(logoutUserSuccess());
            })
          );
        } else {
          return of(logoutUserSuccess());
        }
      })
    );
  });

  refreshUserTokenSuccess$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.refreshUserTokenSuccess),
      withLatestFrom(this.store.select(selectAuthState)),
      tap(([, state]) => {
        if (state.user != null && state.token != null && state.refreshToken != null) {
          this.authService.saveLocalStorage(state.user, state.token, state.refreshToken);
        }
      })
    );
  }, { dispatch: false });

  refreshUserTokenFailure$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.refreshUserTokenFailure),
      tap(() => this.authService.clearLocalStorage())
    );
  }, { dispatch: false });

  private userService: EntityCollectionService<User>;

  constructor(private actions$: Actions,
              private store: Store,
              private authService: AuthService,
              private EntityCollectionServiceFactoryClass: EntityCollectionServiceFactory) {
    this.userService = EntityCollectionServiceFactoryClass.create<User>('User');
  }
}
