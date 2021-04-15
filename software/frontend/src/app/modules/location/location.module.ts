import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LocationRoutingModule } from './location-routing.module';
import { VisualisationComponent } from './pages/visualisation/visualisation.component';
import { SensorListComponent } from './pages/sensor-list/sensor-list.component';


@NgModule({
  declarations: [VisualisationComponent, SensorListComponent],
  imports: [
    CommonModule,
    LocationRoutingModule
  ]
})
export class LocationModule { }
