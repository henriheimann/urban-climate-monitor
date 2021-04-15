import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UserManagementComponent} from './pages/user-management/user-management.component';
import {LocationManagementComponent} from './pages/location-management/location-management.component';
import {SensorManagementComponent} from './pages/sensor-management/sensor-management.component';
import {AuthGuard} from '../auth/guards/auth.guard';
import {RoleGuard} from '../auth/guards/role.guard';

const routes: Routes = [
  {
    path: 'admin',
    redirectTo: 'admin/locations'
  },
  {
    path: 'admin',
    canActivate: [
      AuthGuard,
      RoleGuard
    ],
    data: {
      role: 'ADMIN'
    },
    children: [
      {
        path: 'locations',
        component: LocationManagementComponent
      },
      {
        path: 'sensors',
        component: SensorManagementComponent
      },
      {
        path: 'users',
        component: UserManagementComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
