import {Component, OnInit} from '@angular/core';
import {EntityCollectionService, EntityCollectionServiceFactory} from '@ngrx/data';
import {Observable} from 'rxjs';
import {User} from '../../../shared/models/user.model';
import {Store} from '@ngrx/store';
import {BsModalService} from 'ngx-bootstrap/modal';
import {AddUserModalComponent} from '../../modals/add-user-modal/add-user-modal.component';
import {EditUserModalComponent} from '../../modals/edit-user-modal/edit-user-modal.component';

@Component({
  selector: 'ucm-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {

  users$: Observable<User[]>;
  loading$: Observable<boolean> | Store<boolean>;
  userService: EntityCollectionService<User>;

  constructor(private modalService: BsModalService, EntityCollectionServiceFactoryClass: EntityCollectionServiceFactory) {
    this.userService = EntityCollectionServiceFactoryClass.create<User>('User');
    this.users$ = this.userService.entities$;
    this.loading$ = this.userService.loading$;
  }

  ngOnInit(): void {
    this.userService.getAll();
  }

  onAddUserButtonClicked(): void {
    this.modalService.show(AddUserModalComponent, {class: 'modal-dialog-centered'});
  }

  onEditUserButtonClicked(user: User): void {
    this.modalService.show(EditUserModalComponent, {
      class: 'modal-dialog-centered',
      initialState: { user }
    });
  }

  onDeleteUserButtonClicked(user: User): void {
    this.userService.delete(user);
  }
}
