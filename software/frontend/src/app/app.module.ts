import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
import { AdminModule } from './modules/admin/admin.module';
import { HeaderComponent } from './components/header/header.component';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { DefaultDataServiceConfig, EntityDataModule } from '@ngrx/data';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { AuthModule } from './modules/auth/auth.module';
import { ModalModule } from 'ngx-bootstrap/modal';
import { environment } from '../environments/environment';
import { TokenInterceptor } from './modules/auth/interceptors/token.interceptor';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { AlertModule } from './modules/alert/alert.module';
import { AlertModule as NgxBootstrapAlertModule } from 'ngx-bootstrap/alert';
import { LocationModule } from './modules/location/location.module';
import { FrontPageComponent } from './components/front-page/front-page.component';
import { routerReducer, StoreRouterConnectingModule } from '@ngrx/router-store';
import { FooterComponent } from './components/footer/footer.component';
import { CommonModule } from '@angular/common';
import { ImprintDataProtectionPageComponent } from './components/imprint-data-protection-page/imprint-data-protection-page.component';
import { BackendNotAvailablePageComponent } from './components/backend-not-available-page/backend-not-available-page.component';
import { ErrorInterceptor } from './interceptors/error.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FrontPageComponent,
    ImprintDataProtectionPageComponent,
    FooterComponent,
    BackendNotAvailablePageComponent
  ],
  imports: [
    CommonModule,
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
    StoreModule.forRoot({
      router: routerReducer
    }),
    StoreDevtoolsModule.instrument(),
    StoreRouterConnectingModule.forRoot(),
    EffectsModule.forRoot([]),
    EntityDataModule.forRoot({}),
    AdminModule,
    AuthModule,
    AlertModule,
    LocationModule,
    ModalModule.forRoot(),
    NgxBootstrapAlertModule.forRoot()
  ],
  providers: [
    {
      provide: DefaultDataServiceConfig,
      useValue: {
        root: `${environment.backendUrl}/`
      }
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
