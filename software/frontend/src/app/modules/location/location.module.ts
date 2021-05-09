import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LocationRoutingModule } from './location-routing.module';
import { VisualisationComponent } from './pages/visualisation/visualisation.component';
import { SensorListComponent } from './pages/sensor-list/sensor-list.component';
import { SharedModule } from '../shared/shared.module';
import { LastContactComponent } from './components/last-contact/last-contact.component';
import { MeasurementComponent } from './components/measurement/measurement.component';
import { SensorPopupComponent } from './components/sensor-popup/sensor-popup.component';
import { FormsModule } from '@angular/forms';
import { Vec3FormComponent } from './components/vec3-form/vec3-form.component';
import { EffectsModule } from '@ngrx/effects';
import { LocationEffects } from './store/location.effects';

@NgModule({
  declarations: [
    VisualisationComponent,
    SensorListComponent,
    LastContactComponent,
    MeasurementComponent,
    SensorPopupComponent,
    Vec3FormComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    LocationRoutingModule,
    FormsModule,
    EffectsModule.forFeature([LocationEffects, LocationEffects])
  ]
})
export class LocationModule {}
