import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FrontPageComponent } from './components/front-page/front-page.component';
import { ImprintDataProtectionPageComponent } from './components/imprint-data-protection-page/imprint-data-protection-page.component';
import { BackendNotAvailablePageComponent } from './components/backend-not-available-page/backend-not-available-page.component';

export const routes: Routes = [
  {
    path: '',
    component: FrontPageComponent,
    data: {
      splash: true
    }
  },
  {
    path: 'imprint-data-protection',
    component: ImprintDataProtectionPageComponent
  },
  {
    path: 'backend-not-available',
    component: BackendNotAvailablePageComponent,
    data: {
      splash: true
    }
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
