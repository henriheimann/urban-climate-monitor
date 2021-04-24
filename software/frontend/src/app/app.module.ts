import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {AdminModule} from './modules/admin/admin.module';
import {HeaderComponent} from './components/header/header.component';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {DefaultDataServiceConfig, EntityDataModule} from '@ngrx/data';
import {StoreModule} from '@ngrx/store';
import {EffectsModule} from '@ngrx/effects';
import {AuthModule} from './modules/auth/auth.module';
import {ModalModule} from 'ngx-bootstrap/modal';
import {environment} from '../environments/environment';
import {TokenInterceptor} from './modules/auth/interceptors/token.interceptor';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import {AlertModule} from './modules/alert/alert.module';
import {AlertModule as NgxBootstrapAlertModule} from 'ngx-bootstrap/alert';
import {LocationModule} from './modules/location/location.module';
import {ImprintDataProtectionComponent} from './pages/imprint-data-protection/imprint-data-protection.component';
import {FrontPageComponent} from './pages/front-page/front-page.component';
import {LoginPageComponent} from './modules/admin/pages/login-page/login-page.component';

const customDataServiceConfig: DefaultDataServiceConfig = {
  root: environment.backendUrl + '/'
};

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FrontPageComponent,
    ImprintDataProtectionComponent
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
    StoreModule.forRoot({}),
    StoreDevtoolsModule.instrument(),
    EffectsModule.forRoot([]),
    EntityDataModule.forRoot({}), // Metadata will be added in each feature module by multi-provider
    AdminModule,
    AuthModule,
    ModalModule.forRoot(),
    NgxBootstrapAlertModule.forRoot(),
    AlertModule,
    LocationModule
  ],
  providers: [
    {
      provide: DefaultDataServiceConfig,
      useValue: customDataServiceConfig
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
