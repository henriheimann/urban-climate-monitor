import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Store } from '@ngrx/store';
import { EntityCollectionService, EntityCollectionServiceFactory } from '@ngrx/data';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Location } from '../../../shared/models/location.model';
import { LocationModalComponent } from '../../modals/location-modal/location-modal.component';
import { SafeResourceUrl } from '@angular/platform-browser';
import { environment } from '../../../../../environments/environment';

@Component({
  selector: 'ucm-location-management',
  templateUrl: './location-management.component.html',
  styleUrls: ['./location-management.component.css']
})
export class LocationManagementComponent implements OnInit {
  locations$: Observable<Location[]>;

  loading$: Observable<boolean> | Store<boolean>;

  locationService: EntityCollectionService<Location>;

  constructor(
    private modalService: BsModalService,
    EntityCollectionServiceFactoryClass: EntityCollectionServiceFactory
  ) {
    this.locationService = EntityCollectionServiceFactoryClass.create<Location>('Location');
    this.locations$ = this.locationService.entities$;
    this.loading$ = this.locationService.loading$;
  }

  ngOnInit(): void {
    this.locationService.getAll();
  }

  getLocationIcon(location: Location | undefined): SafeResourceUrl | undefined {
    if (!location) {
      return undefined;
    }
    return environment.backendUrl + location.iconUrl;
  }

  onAddLocationButtonClicked(): void {
    this.modalService.show(LocationModalComponent, {
      class: 'modal-dialog-centered'
    });
  }

  onEditLocationButtonClicked(location: Location): void {
    this.modalService.show(LocationModalComponent, {
      class: 'modal-dialog-centered',
      initialState: { location }
    });
  }

  onDeleteLocationButtonClicked(location: Location): void {
    this.locationService.delete(location);
  }
}
