import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {Store} from '@ngrx/store';
import {selectIsLoggedIn} from '../store/auth.selectors';
import {tap} from 'rxjs/operators';
import {dispatchAlert} from '../../alert/store/alert.actions';
import {Alert} from '../../alert/models/alert.model';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  private loggedIn$ = this.store.select(selectIsLoggedIn);

  constructor(private store: Store, private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.loggedIn$.pipe(tap((loggedIn) => {
      if (!loggedIn) {
        this.store.dispatch(dispatchAlert({
          alert: new Alert('alert.not_logged_in')
        }));
        this.router.navigate(['/']).then();
      }
    }));
  }
}
