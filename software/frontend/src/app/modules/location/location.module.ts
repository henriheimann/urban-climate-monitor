import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {LocationRoutingModule} from './location-routing.module';
import {VisualisationComponent} from './pages/visualisation/visualisation.component';
import {SensorListComponent} from './pages/sensor-list/sensor-list.component';
import {SharedModule} from '../shared/shared.module';
import { LastContactComponent } from './components/last-contact/last-contact.component';
import { MeasurementComponent } from './components/measurement/measurement.component';


@NgModule({
  declarations: [VisualisationComponent, SensorListComponent, LastContactComponent, MeasurementComponent],
  imports: [
    CommonModule,
    SharedModule,
    LocationRoutingModule
  ]
})
export class LocationModule { }
