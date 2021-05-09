import { createAction, createReducer, on } from '@ngrx/store';
import * as AuthActions from './auth.actions';
import { User } from '../../shared/models/user.model';

export const authFeatureKey = 'auth';

export interface AuthState {
  token: string | null;
  refreshToken: string | null;

  user: User | null;

  loggingIn: boolean;
  refreshingToken: boolean;
}

export const initialState: AuthState = {
  token: null,
  refreshToken: null,

  user: null,

  loggingIn: false,
  refreshingToken: false
};

export const reducer = createReducer(
  initialState,

  on(AuthActions.loginUser, (state) => ({
    ...state,
    loggingIn: true
  })),

  on(AuthActions.loginUserGetTokensSuccess, (state, action) => ({
    ...state,
    token: action.token,
    refreshToken: action.refreshToken
  })),

  on(AuthActions.loginUserGetUserSuccess, (state, action) => ({
    ...state,
    user: action.user,
    loggingIn: false
  })),

  on(AuthActions.loginUserFailure, (state) => ({
    ...state,
    user: null,
    token: null,
    refreshToken: null,
    loggingIn: false
  })),

  on(AuthActions.loadUserFromLocalStorage, (state) => ({
    ...state,
    loggingIn: true
  })),

  on(AuthActions.loadUserFromLocalStorageSuccess, (state, action) => ({
    ...state,
    user: action.user,
    token: action.token,
    refreshToken: action.refreshToken,
    loggingIn: false
  })),

  on(AuthActions.loadUserFromLocalStorageFailure, (state) => ({
    ...state,
    user: null,
    token: null,
    refreshToken: null,
    loggingIn: false
  })),

  on(AuthActions.refreshUserToken, (state) => ({
    ...state,
    refreshingToken: true
  })),

  on(AuthActions.refreshUserTokenSuccess, (state, action) => ({
    ...state,
    token: action.token,
    refreshToken: action.refreshToken,
    refreshingToken: false
  })),

  on(AuthActions.refreshUserTokenFailure, (state) => ({
    ...state,
    user: null,
    token: null,
    refreshToken: null,
    refreshingToken: false
  })),

  on(AuthActions.logoutUser, (state) => ({
    ...state
  })),

  on(AuthActions.logoutUserSuccess, (state) => ({
    ...state,
    user: null,
    token: null,
    refreshToken: null
  }))
);
