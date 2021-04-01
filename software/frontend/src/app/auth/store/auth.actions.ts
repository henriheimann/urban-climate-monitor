import { createAction, props } from '@ngrx/store';
import {HttpErrorResponse} from '@angular/common/http';
import {User} from '../../user/models/user.model';

export const loginUser = createAction(
  '[Auth] Login User', props<{ username: string, password: string }>()
);

export const loginUserSuccess = createAction(
  '[Auth] Login User Success', props<{ token: string, user: User }>()
);

export const loginUserFailure = createAction(
  '[Auth] Login User Failure', props<{ error: HttpErrorResponse }>()
);

export const logoutUser = createAction(
  '[Auth] Logout User'
);
