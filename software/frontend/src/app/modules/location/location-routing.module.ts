import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SensorListPageComponent } from './components/sensor-list-page/sensor-list-page.component';
import { VisualisationPageComponent } from './components/visualisation-page/visualisation-page.component';

const routes: Routes = [
  {
    path: 'location/:locationId',
    redirectTo: 'location/:locationId/visualisation'
  },
  {
    path: 'location/:locationId',
    children: [
      {
        path: 'visualisation',
        component: VisualisationPageComponent,
        data: {
          fullcontent: true
        }
      },
      {
        path: 'sensors',
        component: SensorListPageComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LocationRoutingModule {}
