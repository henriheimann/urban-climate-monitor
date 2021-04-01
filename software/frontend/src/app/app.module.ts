import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {VisualisationComponent} from './visualisation/visualisation.component';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {UserModule} from './user/user.module';
import { HeaderComponent } from './components/header/header.component';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {DefaultDataServiceConfig, EntityDataModule} from '@ngrx/data';
import {StoreModule} from '@ngrx/store';
import {EffectsModule} from '@ngrx/effects';
import {AuthModule} from './auth/auth.module';
import {ModalModule} from 'ngx-bootstrap/modal';
import {environment} from '../environments/environment';

const customDataServiceConfig: DefaultDataServiceConfig = {
  root: environment.backendUrl + '/'
};

@NgModule({
  declarations: [
    AppComponent,
    VisualisationComponent,
    HeaderComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (http: HttpClient) => new TranslateHttpLoader(http),
        deps: [HttpClient]
      },
      defaultLanguage: 'en'
    }),
    StoreModule.forRoot({ }),
    EffectsModule.forRoot([]),
    EntityDataModule.forRoot({ }), // Metadata will be added in each feature module by multi-provider
    UserModule,
    AuthModule,
    ModalModule.forRoot()
  ],
  providers: [{ provide: DefaultDataServiceConfig, useValue: customDataServiceConfig }],
  bootstrap: [AppComponent]
})
export class AppModule { }
