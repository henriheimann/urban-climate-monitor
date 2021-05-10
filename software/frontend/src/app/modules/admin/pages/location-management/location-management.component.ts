import { Component, OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { LocationModel } from '../../../shared/models/location.model';
import { LocationModalComponent } from '../../modals/location-modal/location-modal.component';
import { SafeResourceUrl } from '@angular/platform-browser';
import { environment } from '../../../../../environments/environment';
import { LocationService } from '../../../shared/services/location.service';

@Component({
  selector: 'ucm-location-management',
  templateUrl: './location-management.component.html',
  styleUrls: ['./location-management.component.css']
})
export class LocationManagementComponent implements OnInit {
  locations$ = this.locationService.entities$;
  loading$ = this.locationService.loading$;

  constructor(private modalService: BsModalService, private locationService: LocationService) {}

  ngOnInit(): void {
    this.locationService.getAll();
  }

  getLocationIcon(location: LocationModel | undefined): SafeResourceUrl | undefined {
    if (!location) {
      return undefined;
    }
    return environment.backendUrl + location.icon.url;
  }

  onAddLocationButtonClicked(): void {
    this.modalService.show(LocationModalComponent, {
      class: 'modal-dialog-centered',
      ignoreBackdropClick: true
    });
  }

  onEditLocationButtonClicked(location: LocationModel): void {
    this.modalService.show(LocationModalComponent, {
      class: 'modal-dialog-centered',
      ignoreBackdropClick: true,
      initialState: { location }
    });
  }

  onDeleteLocationButtonClicked(location: LocationModel): void {
    this.locationService.delete(location);
  }
}
