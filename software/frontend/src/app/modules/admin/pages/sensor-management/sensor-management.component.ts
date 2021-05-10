import { Component, OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { SensorModel } from '../../../shared/models/sensor.model';
import { SensorModalComponent } from '../../modals/sensor-modal/sensor-modal.component';
import { SensorKeysModalComponent } from '../../modals/sensor-keys-modal/sensor-keys-modal.component';
import { LocationService } from '../../../shared/services/location.service';
import { SensorService } from '../../../shared/services/sensor.service';

@Component({
  selector: 'ucm-sensor-management',
  templateUrl: './sensor-management.component.html',
  styleUrls: ['./sensor-management.component.css']
})
export class SensorManagementComponent implements OnInit {
  sensors$ = this.sensorService.entities$;
  loading$ = this.sensorService.loading$;
  locationsMap$ = this.locationService.entityMap$;

  constructor(private modalService: BsModalService, private sensorService: SensorService, private locationService: LocationService) {}

  ngOnInit(): void {
    this.sensorService.getAll();
    this.locationService.getAll();
  }

  onAddSensorButtonClicked(): void {
    this.modalService.show(SensorModalComponent, { class: 'modal-dialog-centered' });
  }

  onEditSensorButtonClicked(sensor: SensorModel): void {
    this.modalService.show(SensorModalComponent, {
      class: 'modal-dialog-centered',
      initialState: { sensor }
    });
  }

  onDeleteSensorButtonClicked(sensor: SensorModel): void {
    this.sensorService.delete(sensor);
  }

  onShowSensorKeysButtonClicked(sensor: SensorModel): void {
    this.modalService.show(SensorKeysModalComponent, {
      class: 'modal-dialog-centered modal-lg',
      initialState: { sensor }
    });
  }
}
