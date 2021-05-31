import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ENTITY_METADATA_TOKEN } from '@ngrx/data';
import { UserModel } from './models/user.model';
import { MeasurementComponent } from './components/measurement/measurement.component';
import { SensorModel } from './models/sensor.model';

export function sortByName(a: SensorModel, b: SensorModel): number {
  return a.name.localeCompare(b.name);
}

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
          selectId: (user: UserModel) => user.username
        },
        LocationModel: {
          entityName: 'Location'
        },
        SensorModel: {
          entityName: 'Sensor',
          sortComparer: sortByName
        }
      }
    }
  ]
})
export class SharedModule {}
