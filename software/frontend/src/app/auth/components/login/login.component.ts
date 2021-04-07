import {Component, OnInit} from '@angular/core';
import {BsModalRef} from 'ngx-bootstrap/modal';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthState} from '../../store/auth.reducer';
import {Store} from '@ngrx/store';
import {loginUser} from '../../store/auth.actions';
import {selectIsLoggedIn, selectLoggingIn} from '../../store/auth.selectors';
import {clearAlertsForDestination} from '../../../alert/store/alert.actions';

@Component({
  selector: 'ucm-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required)
  });

  loggingIn$ = this.store.select(selectLoggingIn);

  constructor(public modalRef: BsModalRef, private store: Store) { }

  ngOnInit(): void {
    this.loggingIn$.subscribe(loggingIn => loggingIn ? this.loginForm.disable() : this.loginForm.enable());

    this.store.select(selectIsLoggedIn).subscribe(isLoggedIn => {
      if (isLoggedIn) {
        this.modalRef.hide();
      }
    });
  }

  onSubmit(): void {
    this.store.dispatch(clearAlertsForDestination({destination: 'login'}));
    this.store.dispatch(loginUser(this.loginForm.value));
  }
}
