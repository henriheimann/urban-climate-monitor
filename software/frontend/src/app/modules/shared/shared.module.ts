import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ENTITY_METADATA_TOKEN } from '@ngrx/data';
import { UserModel } from './models/user.model';
import { MeasurementComponent } from './components/measurement/measurement.component';
import { SensorModel } from './models/sensor.model';
import { LocationModel } from './models/location.model';

@NgModule({
  declarations: [MeasurementComponent],
  imports: [CommonModule, TranslateModule, ReactiveFormsModule],
  exports: [TranslateModule, ReactiveFormsModule, MeasurementComponent],
  providers: [
    {
      provide: ENTITY_METADATA_TOKEN,
      multi: true,
      useValue: {
        UserModel: {
          entityName: 'User',
          selectId: (user: UserModel) => user.username,
          sortComparer: (a: UserModel, b: UserModel) => a.username.localeCompare(b.username)
        },
        LocationModel: {
          entityName: 'Location',
          sortComparer: (a: LocationModel, b: LocationModel) => a.name.localeCompare(b.name)
        },
        SensorModel: {
          entityName: 'Sensor',
          sortComparer: (a: SensorModel, b: SensorModel) => a.name.localeCompare(b.name)
        }
      }
    }
  ]
})
export class SharedModule {}
