import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Store } from '@ngrx/store';
import { loadUserFromLocalStorage } from './modules/auth/store/auth.actions';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { filter, map } from 'rxjs/operators';

@Component({
  selector: 'ucm-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  splash = false;

  routeData = this.router.events.pipe(
    filter((event) => event instanceof NavigationEnd),
    map(() => this.activatedRoute.root.firstChild?.snapshot.data)
  );

  constructor(
    private translateService: TranslateService,
    private store: Store,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {}

  isVisualisation(): boolean {
    return this.router.url.indexOf('visualisation') !== -1;
  }

  ngOnInit(): void {
    this.translateService.setDefaultLang('en');
    this.translateService.use('en');

    this.store.dispatch(loadUserFromLocalStorage());

    this.routeData.subscribe((data) => {
      this.splash = data?.splash;
    });
  }
}
