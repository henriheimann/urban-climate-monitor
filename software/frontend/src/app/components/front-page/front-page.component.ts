import { Component, OnInit } from '@angular/core';
import { LocationModel } from '../../modules/shared/models/location.model';
import { SafeResourceUrl } from '@angular/platform-browser';
import { environment } from '../../../environments/environment';
import { LocationService } from '../../modules/shared/services/location.service';

@Component({
  selector: 'ucm-index',
  templateUrl: './front-page.component.html',
  styleUrls: ['./front-page.component.scss']
})
export class FrontPageComponent implements OnInit {
  loading$ = this.locationService.loading$;
  locations$ = this.locationService.entities$;

  constructor(private locationService: LocationService) {}

  ngOnInit(): void {
    this.locationService.getAll();
  }

  getLocationIcon(location: LocationModel | undefined): SafeResourceUrl | undefined {
    if (!location) {
      return undefined;
    }
    return environment.backendUrl + location.icon.url;
  }
}
