import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {BehaviorSubject, Observable, of, throwError} from 'rxjs';
import {Store} from '@ngrx/store';
import {selectAuthState, selectToken} from '../store/auth.selectors';
import {catchError, filter, mergeMap, switchMap, take} from 'rxjs/operators';
import {refreshUserToken} from '../store/auth.actions';
import {AuthEffects} from '../store/auth.effects';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  private token$ = new BehaviorSubject<string | null>(null);

  constructor(private store: Store) {
    this.store.select(selectToken).subscribe(this.token$);
  }

  private static addToken(request: HttpRequest<any>, token: string): any {
    return request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    if (request.url.includes('oauth')) {
      return next.handle(request);
    }

    const token = this.token$.getValue();
    if (token) {
      request = TokenInterceptor.addToken(request, token);
    }

    return next.handle(request).pipe(catchError(error => {
      if (error.status === 401) {
        this.store.dispatch(refreshUserToken());
        return this.store.select(selectAuthState).pipe(
          filter(state => !state.refreshingToken),
          take(1),
          switchMap((state) => {
            if (state.token != null) {
              return next.handle(TokenInterceptor.addToken(request, state.token));
            } else {
              return throwError(error);
            }
          })
        );
      } else {
        return throwError(error);
      }
    }));
  }
}
