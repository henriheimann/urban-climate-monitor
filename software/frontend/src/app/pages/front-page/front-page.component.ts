import { Component, OnInit } from '@angular/core';
import { EntityCollectionService, EntityCollectionServiceFactory } from '@ngrx/data';
import { Location } from '../../modules/shared/models/location.model';
import { Observable } from 'rxjs';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'ucm-index',
  templateUrl: './front-page.component.html',
  styleUrls: ['./front-page.component.scss']
})
export class FrontPageComponent implements OnInit {
  locationService: EntityCollectionService<Location>;

  locations$: Observable<Location[]>;

  constructor(EntityCollectionServiceFactoryClass: EntityCollectionServiceFactory, private sanitizer: DomSanitizer) {
    this.locationService = EntityCollectionServiceFactoryClass.create<Location>('Location');
    this.locations$ = this.locationService.entities$;
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
}
