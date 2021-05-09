import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MustMatch } from '../../validators/must-match.validator';
import { Observable } from 'rxjs';
import { Location } from '../../../shared/models/location.model';
import { EntityCollectionService, EntityCollectionServiceFactory } from '@ngrx/data';
import { User } from '../../../shared/models/user.model';
import { BsModalRef } from 'ngx-bootstrap/modal';

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

  user: User | undefined;

  locations$: Observable<Location[]>;

  locationService: EntityCollectionService<Location>;

  userService: EntityCollectionService<User>;

  constructor(public modalRef: BsModalRef, EntityCollectionServiceFactoryClass: EntityCollectionServiceFactory) {
    this.locationService = EntityCollectionServiceFactoryClass.create<Location>('Location');
    this.userService = EntityCollectionServiceFactoryClass.create<User>('User');
    this.locations$ = this.locationService.entities$;
  }

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
