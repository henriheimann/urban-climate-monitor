import { Action, createReducer, on } from '@ngrx/store';
import * as AuthActions from './auth.actions';
import {User} from '../../user/models/user.model';

export const authFeatureKey = 'auth';

export interface AuthState {
  token: string | null;
  user: User | null;

  loggingIn: boolean;
}

export const initialState: AuthState = {
  token: null,
  user: null,

  loggingIn: false
};


export const reducer = createReducer(
  initialState,

  on(AuthActions.loginUser, state => {
    return {
      ...state,
      loggingIn: true
    };
  }),

  on(AuthActions.loginUserSuccess, (state, action) => {
    return {
      ...state,
      token: action.token,
      user: action.user,
      loggingIn: false
    };
  }),

  on(AuthActions.loginUserFailure, state => {
    return {
      ...state,
      loggingIn: false
    };
  }),

  on(AuthActions.logoutUser, state => {
    return {
      ...state,
      token: null,
      user: null
    };
  })
);

