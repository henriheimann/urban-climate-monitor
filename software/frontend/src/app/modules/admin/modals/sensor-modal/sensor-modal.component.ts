import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { EntityCollectionService, EntityCollectionServiceFactory } from '@ngrx/data';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Sensor } from '../../../shared/models/sensor.model';
import { Observable } from 'rxjs';
import { Location } from '../../../shared/models/location.model';

@Component({
  selector: 'ucm-sensor-modal',
  templateUrl: './sensor-modal.component.html',
  styleUrls: ['./sensor-modal.component.css']
})
export class SensorModalComponent implements OnInit {
  sensorForm = new FormGroup({
    name: new FormControl('', Validators.required),
    location: new FormControl(null)
  });

  sensorService: EntityCollectionService<Sensor>;

  locations$: Observable<Location[]>;

  locationService: EntityCollectionService<Location>;

  constructor(public modalRef: BsModalRef, EntityCollectionServiceFactoryClass: EntityCollectionServiceFactory) {
    this.sensorService = EntityCollectionServiceFactoryClass.create<Sensor>('Sensor');
    this.locationService = EntityCollectionServiceFactoryClass.create<Location>('Location');
    this.locations$ = this.locationService.entities$;
  }

  sensor: Sensor | undefined;

  switchOnModelType(addKey: string, editKey: string): string {
    if (this.sensor !== undefined) {
      return editKey;
    }
    return addKey;
  }

  ngOnInit(): void {
    this.locationService.getAll();
    if (this.sensor) {
      this.sensorForm.setValue({
        name: this.sensor.name,
        location: this.sensor.locationId
      });
    }
  }

  onSubmit(): void {
    if (this.sensor) {
      this.sensorService
        .update({
          id: this.sensor.id,
          name: this.sensorForm.get('name')?.value,
          ttnId: this.sensor.ttnId,
          locationId: this.sensorForm.get('location')?.value
        })
        .subscribe(() => {
          this.modalRef.hide();
        });
    } else {
      this.sensorService
        .add({
          id: null,
          name: this.sensorForm.get('name')?.value,
          ttnId: null,
          locationId: this.sensorForm.get('location')?.value
        })
        .subscribe(() => {
          this.modalRef.hide();
        });
    }
  }
}
