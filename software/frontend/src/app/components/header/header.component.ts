import { Component, OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Store } from '@ngrx/store';
import { selectLoggedInUser } from '../../modules/auth/store/auth.selectors';
import { logoutUser } from '../../modules/auth/store/auth.actions';
import { Router } from '@angular/router';
import { EntityCollectionService, EntityCollectionServiceFactory } from '@ngrx/data';
import { Location } from '../../modules/shared/models/location.model';
import { Observable } from 'rxjs';
import { Dictionary } from '@ngrx/entity';
import { map } from 'rxjs/operators';
import { SafeResourceUrl } from '@angular/platform-browser';
import { LoginModalComponent } from '../../modules/auth/modals/login-modal/login-modal.component';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'ucm-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  user$ = this.store.select(selectLoggedInUser);

  locationService: EntityCollectionService<Location>;

  locations$: Observable<Location[]>;

  locationsMap$: Observable<Dictionary<Location>>;

  constructor(
    private modalService: BsModalService,
    private store: Store,
    private router: Router,
    EntityCollectionServiceFactoryClass: EntityCollectionServiceFactory
  ) {
    this.locationService = EntityCollectionServiceFactoryClass.create<Location>('Location');
    this.locations$ = this.locationService.entities$;
    this.locationsMap$ = this.locationService.entityMap$;
  }

  onLoginButtonClicked(): void {
    this.modalService.show(LoginModalComponent, { class: 'modal-dialog-centered' });
  }

  onLogout(): void {
    this.store.dispatch(logoutUser());
  }

  routeStartsWith(route: string): boolean {
    return this.router.url.startsWith(route);
  }

  getRouteLocation(): Observable<Location | undefined> {
    const locationId = this.getRouteLocationId();
    return this.locationsMap$.pipe(map((locationsMap) => locationsMap[locationId]));
  }

  getLocationIcon(location: Location | undefined): SafeResourceUrl | undefined {
    if (!location) {
      return undefined;
    }
    return environment.backendUrl + location.iconUrl;
  }

  getRouteLocationIcon(): Observable<SafeResourceUrl | undefined> {
    return this.getRouteLocation().pipe(map((location) => this.getLocationIcon(location)));
  }

  getRouteLocationId(): number {
    return parseInt(this.router.url.split('/')[2], 10);
  }

  ngOnInit(): void {
    this.locationService.getAll();
  }
}
