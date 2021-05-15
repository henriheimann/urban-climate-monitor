import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LocationRoutingModule } from './location-routing.module';
import { SharedModule } from '../shared/shared.module';
import { LastContactComponent } from './components/last-contact/last-contact.component';
import { MeasurementComponent } from './components/measurement/measurement.component';
import { FormsModule } from '@angular/forms';
import { EffectsModule } from '@ngrx/effects';
import { LocationEffects } from './store/location.effects';
import { SensorListPageComponent } from './components/sensor-list-page/sensor-list-page.component';
import { VisualisationSensorDetailComponent } from './components/sensor-visualisation-detail/visualisation-sensor-detail.component';
import { Vec3Component } from './components/vec3/vec3.component';
import { VisualisationPageComponent } from './components/visualisation-page/visualisation-page.component';
import { StoreModule } from '@ngrx/store';
import * as fromLocation from './store/location.reducer';
import { MeasurementsSelectorComponent } from './components/measurements-selector/measurements-selector.component';
import { NgxSliderModule } from '@angular-slider/ngx-slider';

@NgModule({
  declarations: [
    SensorListPageComponent,
    LastContactComponent,
    MeasurementComponent,
    Vec3Component,
    VisualisationPageComponent,
    VisualisationSensorDetailComponent,
    MeasurementsSelectorComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    LocationRoutingModule,
    FormsModule,
    NgxSliderModule,
    StoreModule.forFeature(fromLocation.locationFeatureKey, fromLocation.reducer),
    EffectsModule.forFeature([LocationEffects])
  ]
})
export class LocationModule {}
