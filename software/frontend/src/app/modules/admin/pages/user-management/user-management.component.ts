import { Component, OnInit } from '@angular/core';
import { UserModel } from '../../../shared/models/user.model';
import { BsModalService } from 'ngx-bootstrap/modal';
import { AddUserModalComponent } from '../../modals/add-user-modal/add-user-modal.component';
import { EditUserModalComponent } from '../../modals/edit-user-modal/edit-user-modal.component';
import { UserService } from '../../../shared/services/user.service';

@Component({
  selector: 'ucm-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {
  users$ = this.userService.entities$;
  loading$ = this.userService.loading$;

  constructor(private modalService: BsModalService, private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getAll();
  }

  onAddUserButtonClicked(): void {
    this.modalService.show(AddUserModalComponent, { class: 'modal-dialog-centered' });
  }

  onEditUserButtonClicked(user: UserModel): void {
    this.modalService.show(EditUserModalComponent, {
      class: 'modal-dialog-centered',
      initialState: { user }
    });
  }

  onDeleteUserButtonClicked(user: UserModel): void {
    this.userService.delete(user);
  }
}
