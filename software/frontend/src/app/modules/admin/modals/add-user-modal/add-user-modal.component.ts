import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { MustMatch } from '../../validators/must-match.validator';
import { Observable } from 'rxjs';
import { EntityCollectionService, EntityCollectionServiceFactory } from '@ngrx/data';
import { Location } from '../../../shared/models/location.model';
import { User } from '../../../shared/models/user.model';

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
  }

  onSubmit(): void {
    const user: User = {
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
