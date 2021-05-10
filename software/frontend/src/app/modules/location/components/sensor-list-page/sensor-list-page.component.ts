import { Component } from '@angular/core';
import { LocationService } from '../../../shared/services/location.service';
import { ActivatedRoute } from '@angular/router';
import { filter, map, switchMap } from 'rxjs/operators';

@Component({
  selector: 'ucm-sensor-list',
  templateUrl: './sensor-list-page.component.html',
  styleUrls: ['./sensor-list-page.component.css']
})
export class SensorListPageComponent {
  location$ = this.activatedRoute.params.pipe(
    filter((params) => params?.locationId != undefined),
    map((params) => params.locationId),
    switchMap((locationId) => this.locationService.getByKey(locationId))
  );

  constructor(private locationService: LocationService, private activatedRoute: ActivatedRoute) {}
}
