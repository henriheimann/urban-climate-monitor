import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Location } from '../../../shared/models/location.model';
import { Store } from '@ngrx/store';
import { EntityCollectionService, EntityCollectionServiceFactory } from '@ngrx/data';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Sensor } from '../../../shared/models/sensor.model';
import { SensorModalComponent } from '../../modals/sensor-modal/sensor-modal.component';
import { Dictionary } from '@ngrx/entity';
import { SensorKeysModalComponent } from '../../modals/sensor-keys-modal/sensor-keys-modal.component';

@Component({
  selector: 'ucm-sensor-management',
  templateUrl: './sensor-management.component.html',
  styleUrls: ['./sensor-management.component.css']
})
export class SensorManagementComponent implements OnInit {
  sensors$: Observable<Sensor[]>;

  loading$: Observable<boolean> | Store<boolean>;

  locationsMap$: Observable<Dictionary<Location>>;

  sensorService: EntityCollectionService<Sensor>;

  locationService: EntityCollectionService<Location>;

  constructor(
    private modalService: BsModalService,
    EntityCollectionServiceFactoryClass: EntityCollectionServiceFactory
  ) {
    this.sensorService = EntityCollectionServiceFactoryClass.create<Sensor>('Sensor');
    this.sensors$ = this.sensorService.entities$;
    this.loading$ = this.sensorService.loading$;

    this.locationService = EntityCollectionServiceFactoryClass.create<Location>('Location');
    this.locationsMap$ = this.locationService.entityMap$;
  }

  ngOnInit(): void {
    this.sensorService.getAll();
    this.locationService.getAll();
  }

  onAddSensorButtonClicked(): void {
    this.modalService.show(SensorModalComponent, { class: 'modal-dialog-centered' });
  }

  onEditSensorButtonClicked(sensor: Sensor): void {
    this.modalService.show(SensorModalComponent, {
      class: 'modal-dialog-centered',
      initialState: { sensor }
    });
  }

  onDeleteSensorButtonClicked(sensor: Sensor): void {
    this.sensorService.delete(sensor);
  }

  onShowSensorKeysButtonClicked(sensor: Sensor): void {
    this.modalService.show(SensorKeysModalComponent, {
      class: 'modal-dialog-centered modal-lg',
      initialState: { sensor }
    });
  }
}
