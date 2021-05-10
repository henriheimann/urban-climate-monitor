import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UserModel } from '../../../shared/models/user.model';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { LocationService } from '../../../shared/services/location.service';
import { UserService } from '../../../shared/services/user.service';

@Component({
  selector: 'ucm-edit-user-modal',
  templateUrl: './edit-user-modal.component.html',
  styleUrls: ['./edit-user-modal.component.css']
})
export class EditUserModalComponent implements OnInit {
  addUserForm = new FormGroup({
    isAdmin: new FormControl(false, Validators.required),
    locationsWithPermission: new FormControl([])
  });

  user: UserModel | undefined;

  locations$ = this.locationService.entities$;

  constructor(public modalRef: BsModalRef, private locationService: LocationService, private userService: UserService) {}

  ngOnInit(): void {
    this.locationService.getAll();

    this.addUserForm.setValue({
      isAdmin: this.user?.role === 'ADMIN',
      locationsWithPermission: this.user?.locationsWithPermission
    });
  }

  onSubmit(): void {
    const editedUser = {
      ...this.user,
      role: this.addUserForm.get('isAdmin')?.value === true ? 'ADMIN' : 'USER',
      locationsWithPermission: this.addUserForm.get('locationsWithPermission')?.value
    };

    this.userService.update(editedUser).subscribe(() => {
      this.modalRef.hide();
    });
  }
}
