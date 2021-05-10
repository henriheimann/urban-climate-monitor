import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { SensorModel } from '../../../shared/models/sensor.model';
import { Observable } from 'rxjs';
import { LocationModel } from '../../../shared/models/location.model';
import { SensorService } from '../../../shared/services/sensor.service';
import { LocationService } from '../../../shared/services/location.service';

@Component({
  selector: 'ucm-sensor-modal',
  templateUrl: './sensor-modal.component.html',
  styleUrls: ['./sensor-modal.component.css']
})
export class SensorModalComponent implements OnInit {
  sensorForm = new FormGroup({
    name: new FormControl('', Validators.required),
    locationId: new FormControl(null),

    // Hidden controls
    id: new FormControl(null),
    position: new FormControl([0.0, 0.0, 0.0]),
    rotation: new FormControl([0.0, 0.0, 0.0])
  });

  locations$: Observable<LocationModel[]> = this.locationService.entities$;
  sensor: SensorModel | undefined;

  constructor(public modalRef: BsModalRef, private sensorService: SensorService, private locationService: LocationService) {}

  switchOnModelType(addKey: string, editKey: string): string {
    if (this.sensor !== undefined) {
      return editKey;
    }
    return addKey;
  }

  ngOnInit(): void {
    this.locationService.getAll();
    if (this.sensor) {
      this.sensorForm.patchValue(this.sensor);
    }
  }

  onSubmit(): void {
    if (this.sensor) {
      this.sensorService.update(this.sensorForm.value).subscribe(() => {
        this.modalRef.hide();
      });
    } else {
      this.sensorService.add(this.sensorForm.value).subscribe(() => {
        this.modalRef.hide();
      });
    }
  }
}
