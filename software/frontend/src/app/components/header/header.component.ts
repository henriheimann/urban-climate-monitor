import { Component, OnInit } from '@angular/core';
import {BsModalService} from 'ngx-bootstrap/modal';
import {LoginComponent} from '../../auth/components/login/login.component';

@Component({
  selector: 'ucm-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(private modalService: BsModalService) { }

  ngOnInit(): void {
  }

  openLoginModal(): void {
    this.modalService.show(LoginComponent, {class: 'modal-dialog-centered'});
  }
}
