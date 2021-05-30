import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Store } from '@ngrx/store';
import { loadUserFromLocalStorage } from './modules/auth/store/auth.actions';
import { ActivatedRouteSnapshot, NavigationEnd, Router } from '@angular/router';
import { filter, map } from 'rxjs/operators';

@Component({
  selector: 'ucm-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  fullscreen = false;
  fullcontent = false;

  routeData = this.router.events.pipe(
    filter((event) => event instanceof NavigationEnd),
    map(() => {
      const getParams = (route: ActivatedRouteSnapshot): any => ({
        ...route.data,
        ...route.children.reduce((acc, child) => ({ ...getParams(child), ...acc }), {})
      });
      return getParams(this.router.routerState.snapshot.root);
    })
  );

  constructor(private translateService: TranslateService, private store: Store, private router: Router) {}

  ngOnInit(): void {
    this.translateService.setDefaultLang('en');
    this.translateService.use('en');

    this.store.dispatch(loadUserFromLocalStorage());

    this.routeData.subscribe((data) => {
      this.fullscreen = data?.fullscreen || false;
      this.fullcontent = data?.fullcontent || false;
    });
  }
}
