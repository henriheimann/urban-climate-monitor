import {Component} from '@angular/core';
import {BsModalRef} from 'ngx-bootstrap/modal';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthState} from '../../store/auth.reducer';
import {Store} from '@ngrx/store';
import {loginUser} from '../../store/auth.actions';

@Component({
  selector: 'ucm-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  loginForm = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required)
  });

  loading$ = this.store.select('loggingIn');

  constructor(public modalRef: BsModalRef, private store: Store<AuthState>) { }

  onSubmit(): void {
    this.store.dispatch(loginUser(this.loginForm.value));
  }
}
