import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {FrontPageComponent} from './pages/front-page/front-page.component';
import {ImprintDataProtectionComponent} from './pages/imprint-data-protection/imprint-data-protection.component';

const routes: Routes = [
  {
    path: '',
    component: FrontPageComponent
  },
  {
    path: 'imprint-data-protection',
    component: ImprintDataProtectionComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
