import {Component, OnInit} from '@angular/core';
import {EntityCollectionService, EntityCollectionServiceFactory} from '@ngrx/data';
import {Location} from '../../modules/shared/models/location.model';
import {Observable} from 'rxjs';
import {DomSanitizer, SafeResourceUrl} from '@angular/platform-browser';

@Component({
  selector: 'ucm-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit {

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
    return this.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + location.icon.data);
  }
}
