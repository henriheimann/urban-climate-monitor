import { Component, OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { LoginModalComponent } from '../../../auth/modals/login-modal/login-modal.component';
import { Store } from '@ngrx/store';
import { selectIsLoggedIn } from '../../../auth/store/auth.selectors';
import { take } from 'rxjs/operators';
import { Router } from '@angular/router';

@Component({
  selector: 'ucm-login',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {
  isLoggedIn$ = this.store.select(selectIsLoggedIn);

  constructor(private modalService: BsModalService, private store: Store, private router: Router) {}

  ngOnInit(): void {
    this.isLoggedIn$.pipe(take(1)).subscribe((isLoggedIn) => {
      if (!isLoggedIn) {
        this.modalService.show(LoginModalComponent, {
          class: 'modal-dialog-centered',
          ignoreBackdropClick: true,
          initialState: {
            abortRedirect: '',
            successRedirect: 'admin'
          }
        });
      } else {
        this.router.navigate(['admin']).then();
      }
    });
  }
}
