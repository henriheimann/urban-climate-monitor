import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserManagementComponent } from './pages/user-management/user-management.component';
import { AdminRoutingModule } from './admin-routing.module';
import { SharedModule } from '../shared/shared.module';
import { LocationManagementComponent } from './pages/location-management/location-management.component';
import { SensorManagementComponent } from './pages/sensor-management/sensor-management.component';
import { AddUserModalComponent } from './modals/add-user-modal/add-user-modal.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { EditUserModalComponent } from './modals/edit-user-modal/edit-user-modal.component';
import { LocationModalComponent } from './modals/location-modal/location-modal.component';
import { SensorModalComponent } from './modals/sensor-modal/sensor-modal.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { SensorKeysModalComponent } from './modals/sensor-keys-modal/sensor-keys-modal.component';
import { HexDisplayComponent } from './components/hex-display/hex-display.component';
import { UploadModule } from '../upload/upload.module';

@NgModule({
  imports: [CommonModule, SharedModule, AdminRoutingModule, NgSelectModule, UploadModule],
  declarations: [
    UserManagementComponent,
    LocationManagementComponent,
    SensorManagementComponent,
    AddUserModalComponent,
    EditUserModalComponent,
    LocationModalComponent,
    SensorModalComponent,
    LoginPageComponent,
    SensorKeysModalComponent,
    HexDisplayComponent
  ]
})
export class AdminModule {}
