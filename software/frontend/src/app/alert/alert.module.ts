import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EffectsModule } from '@ngrx/effects';
import { AlertEffects } from './store/alert.effects';
import { AlertDisplayComponent } from './components/alert-display/alert-display.component';
import {StoreModule} from '@ngrx/store';
import * as fromAlert from './store/alert.reducer';



@NgModule({
  declarations: [AlertDisplayComponent],
  exports: [
    AlertDisplayComponent
  ],
  imports: [
    CommonModule,
    EffectsModule.forFeature([AlertEffects]),
    StoreModule.forFeature(fromAlert.alertFeatureKey, fromAlert.reducer)
  ]
})
export class AlertModule { }
