import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ENTITY_METADATA_TOKEN } from '@ngrx/data';
import { User } from './models/user.model';
import { UserManagementComponent } from './pages/user-management/user-management.component';
import { UserRoutingModule } from './user-routing.module';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    UserRoutingModule
  ],
  declarations: [
    UserManagementComponent
  ],
  providers: [
    {
      provide: ENTITY_METADATA_TOKEN,
      multi: true,
      useValue: {
        User: {
          selectId: (user: User) => user.username
        }
      }
    }
  ]
})
export class UserModule { }
