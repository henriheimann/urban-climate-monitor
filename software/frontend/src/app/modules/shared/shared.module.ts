import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ENTITY_METADATA_TOKEN } from '@ngrx/data';
import { UserModel } from './models/user.model';

@NgModule({
  declarations: [],
  imports: [CommonModule, TranslateModule, ReactiveFormsModule],
  exports: [TranslateModule, ReactiveFormsModule],
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
          entityName: 'Sensor'
        }
      }
    }
  ]
})
export class SharedModule {}