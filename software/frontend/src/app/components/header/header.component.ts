import { Component, OnInit } from '@angular/core';
import {BsModalService} from 'ngx-bootstrap/modal';
import {LoginComponent} from '../../auth/components/login/login.component';
import {Store} from '@ngrx/store';
import {selectLoggedInUser} from '../../auth/store/auth.selectors';
import {logoutUser} from '../../auth/store/auth.actions';

@Component({
  selector: 'ucm-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  user$ = this.store.select(selectLoggedInUser);

  constructor(private modalService: BsModalService, private store: Store) { }

  openLoginModal(): void {
    this.modalService.show(LoginComponent, {class: 'modal-dialog-centered'});
  }

  onLogout(): void {
    this.store.dispatch(logoutUser());
  }
}
