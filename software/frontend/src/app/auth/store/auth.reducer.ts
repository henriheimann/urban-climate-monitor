import {createAction, createReducer, on} from '@ngrx/store';
import * as AuthActions from './auth.actions';
import {User} from '../../user/models/user.model';

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

  on(AuthActions.loginUser, state => {
    return {
      ...state,
      loggingIn: true
    };
  }),

  on(AuthActions.loginUserGetTokensSuccess, (state, action) => {
    return {
      ...state,
      token: action.token,
      refreshToken: action.refreshToken
    };
  }),

  on(AuthActions.loginUserGetUserSuccess, (state, action) => {
    return {
      ...state,
      user: action.user,
      loggingIn: false
    };
  }),

  on(AuthActions.loginUserFailure, state => {
    return {
      ...state,
      user: null,
      token: null,
      refreshToken: null,
      loggingIn: false
    };
  }),

  on(AuthActions.loadUserFromLocalStorage, state => {
    return {
      ...state,
      loggingIn: true
    };
  }),

  on(AuthActions.loadUserFromLocalStorageSuccess, (state, action) => {
    return {
      ...state,
      user: action.user,
      token: action.token,
      refreshToken: action.refreshToken,
      loggingIn: false
    };
  }),

  on(AuthActions.loadUserFromLocalStorageFailure, state => {
    return {
      ...state,
      user: null,
      token: null,
      refreshToken: null,
      loggingIn: false
    };
  }),

  on(AuthActions.refreshUserToken, state => {
    return {
      ...state,
      refreshingToken: true
    };
  }),

  on(AuthActions.refreshUserTokenSuccess, (state, action) => {
    return {
      ...state,
      token: action.token,
      refreshToken: action.refreshToken,
      refreshingToken: false
    };
  }),

  on(AuthActions.refreshUserTokenFailure, state => {
    return {
      ...state,
      user: null,
      token: null,
      refreshToken: null,
      refreshingToken: false
    };
  }),

  on(AuthActions.logoutUser, state => {
    return {
      ...state
    };
  }),

  on(AuthActions.logoutUserSuccess, state => {
    return {
      ...state,
      user: null,
      token: null,
      refreshToken: null
    };
  })
);

