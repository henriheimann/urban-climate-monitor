import { Component } from '@angular/core';
import { LocationService } from '../../../shared/services/location.service';
import { ActivatedRoute } from '@angular/router';
import { filter, map, switchMap } from 'rxjs/operators';
import { SensorModel } from '../../../shared/models/sensor.model';

@Component({
  selector: 'ucm-sensor-list',
  templateUrl: './sensor-list-page.component.html',
  styleUrls: ['./sensor-list-page.component.css']
})
export class SensorListPageComponent {
  sensors$ = this.activatedRoute.params.pipe(
    filter((params) => params?.locationId != undefined),
    map((params) => params.locationId),
    switchMap((locationId) => this.locationService.getByKey(locationId)),
    map((location) => {
      if (location == undefined) {
        return undefined;
      } else {
        return [...location.sensors].sort((a: SensorModel, b: SensorModel) => {
          return a.name.localeCompare(b.name);
        });
      }
    })
  );

  constructor(private locationService: LocationService, private activatedRoute: ActivatedRoute) {}
}
