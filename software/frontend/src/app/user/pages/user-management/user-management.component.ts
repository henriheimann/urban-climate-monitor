import { Component, OnInit } from '@angular/core';
import {EntityCollectionService, EntityCollectionServiceFactory} from '@ngrx/data';
import {Observable} from 'rxjs';
import {User} from '../../models/user.model';
import {Store} from '@ngrx/store';

@Component({
  selector: 'ucm-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {

  users$: Observable<User[]>;
  loading$: Observable<boolean> | Store<boolean>;
  userService: EntityCollectionService<User>;

  constructor(EntityCollectionServiceFactoryClass: EntityCollectionServiceFactory) {
    this.userService = EntityCollectionServiceFactoryClass.create<User>('User');
    this.users$ = this.userService.entities$;
    this.loading$ = this.userService.loading$;
  }

  ngOnInit(): void {
    this.userService.getAll();
  }
}
