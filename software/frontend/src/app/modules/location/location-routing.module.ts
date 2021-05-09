import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { VisualisationComponent } from './pages/visualisation/visualisation.component';
import { SensorListComponent } from './pages/sensor-list/sensor-list.component';

const routes: Routes = [
  {
    path: 'location/:id',
    redirectTo: 'location/:id/visualisation'
  },
  {
    path: 'location/:id',
    children: [
      {
        path: 'visualisation',
        component: VisualisationComponent
      },
      {
        path: 'sensors',
        component: SensorListComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LocationRoutingModule {}
