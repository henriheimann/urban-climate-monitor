import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { MustMatch } from '../../validators/must-match.validator';
import { UserModel } from '../../../shared/models/user.model';
import { LocationService } from '../../../shared/services/location.service';
import { UserService } from '../../../shared/services/user.service';

@Component({
  selector: 'ucm-user-modal',
  templateUrl: './add-user-modal.component.html',
  styleUrls: ['./add-user-modal.component.css']
})
export class AddUserModalComponent implements OnInit {
  addUserForm = new FormGroup(
    {
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),
      confirmPassword: new FormControl('', Validators.required),
      isAdmin: new FormControl(false, Validators.required),
      locationsWithPermission: new FormControl([])
    },
    {
      validators: MustMatch('password', 'confirmPassword')
    }
  );

  locations$ = this.locationService.entities$;

  constructor(public modalRef: BsModalRef, private locationService: LocationService, private userService: UserService) {}

  ngOnInit(): void {
    this.locationService.getAll();
  }

  onSubmit(): void {
    const user: UserModel = {
      username: this.addUserForm.get('username')?.value,
      password: this.addUserForm.get('password')?.value,
      role: this.addUserForm.get('isAdmin')?.value === true ? 'ADMIN' : 'USER',
      locationsWithPermission: this.addUserForm.get('locationsWithPermission')?.value
    };

    this.userService.add(user).subscribe(() => {
      this.modalRef.hide();
    });
  }
}
