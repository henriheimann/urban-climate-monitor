import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {selectLoggedInUser} from '../store/auth.selectors';
import {Store} from '@ngrx/store';
import {map} from 'rxjs/operators';
import {dispatchAlert} from '../../alert/store/alert.actions';
import {Alert} from '../../alert/models/alert.model';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  private loggedInUser$ = this.store.select(selectLoggedInUser);

  constructor(private store: Store, private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    const role = route.data.role;
    return this.loggedInUser$.pipe(map((loggedInUser) => {
      if (loggedInUser?.role !== role) {
        this.store.dispatch(dispatchAlert({
          alert: new Alert('alert.wrong_role')
        }));
        this.router.navigate(['/']).then();
      }
      return loggedInUser?.role === role;
    }));
  }
}
