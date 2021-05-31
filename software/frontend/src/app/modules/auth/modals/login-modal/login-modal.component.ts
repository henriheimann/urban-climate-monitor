import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { loginUser } from '../../store/auth.actions';
import { selectIsLoggedIn, selectLoggingIn } from '../../store/auth.selectors';
import { clearAlertsForDestination } from '../../../alert/store/alert.actions';
import { Router } from '@angular/router';

@Component({
  selector: 'ucm-login',
  templateUrl: './login-modal.component.html',
  styleUrls: ['./login-modal.component.css']
})
export class LoginModalComponent implements OnInit {
  loginForm = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required)
  });

  loggingIn$ = this.store.select(selectLoggingIn);

  isLoggedIn$ = this.store.select(selectIsLoggedIn);

  successRedirect: string | null = null;

  abortRedirect: string | null = null;

  constructor(public modalRef: BsModalRef, private store: Store, private router: Router) {}

  ngOnInit(): void {
    this.store.dispatch(clearAlertsForDestination({ destination: 'login' }));
    this.loggingIn$.subscribe((loggingIn) => (loggingIn ? this.loginForm.disable() : this.loginForm.enable()));
    this.isLoggedIn$.subscribe((isLoggedIn) => {
      if (isLoggedIn) {
        this.modalRef.hide();
        if (this.successRedirect != null) {
          this.router.navigate(['admin']).then();
        }
      }
    });
  }

  onSubmit(): void {
    this.store.dispatch(clearAlertsForDestination({ destination: 'login' }));
    this.store.dispatch(loginUser(this.loginForm.value));
  }

  onCloseButtonClicked(): void {
    this.modalRef.hide();
    if (this.abortRedirect != null) {
      this.router.navigate(['']).then();
    }
  }
}
