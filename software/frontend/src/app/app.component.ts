import {Component, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {Store} from '@ngrx/store';
import {loadUserFromLocalStorage} from './modules/auth/store/auth.actions';
import {Router} from '@angular/router';

@Component({
  selector: 'ucm-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  constructor(translate: TranslateService, private store: Store, private router: Router) {
    translate.setDefaultLang('en');
    translate.use('en');
  }

  isIndex(): boolean {
    return this.router.url === '/';
  }

  ngOnInit(): void {
    this.store.dispatch(loadUserFromLocalStorage());
  }
}
