import {Component, OnInit} from '@angular/core';
import {BsModalRef} from 'ngx-bootstrap/modal';
import {SensorKeysService} from '../../services/sensor-keys.service';
import {Observable} from 'rxjs';
import {SensorKeys} from '../../models/sensor-keys.model';
import {Sensor} from '../../../shared/models/sensor.model';

@Component({
  selector: 'ucm-sensor-keys-modal',
  templateUrl: './sensor-keys-modal.component.html',
  styleUrls: ['./sensor-keys-modal.component.css']
})
export class SensorKeysModalComponent implements OnInit {

  sensor: Sensor | null = null;
  sensorKeys$: Observable<SensorKeys> | null = null;

  constructor(public modalRef: BsModalRef, private sensorKeysService: SensorKeysService) { }

  ngOnInit(): void {
    if (this.sensor != null && this.sensor.id != null) {
      this.sensorKeys$ = this.sensorKeysService.getSensorKeys(this.sensor.id);
    }
  }
}
