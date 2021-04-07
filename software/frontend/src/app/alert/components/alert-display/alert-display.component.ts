import {Component, Input, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {selectAlertsForDestination} from '../../store/alert.selectors';
import {Observable} from 'rxjs';
import {Alert} from '../../models/alert.model';

@Component({
  selector: 'ucm-alert-display',
  templateUrl: './alert-display.component.html',
  styleUrls: ['./alert-display.component.css']
})
export class AlertDisplayComponent {

  destinationName = '';
  alerts$: Observable<Alert[]> | undefined = undefined;

  @Input()
  set destination(name: string) {
    this.destinationName = name;
    this.alerts$ = this.store.select(state => selectAlertsForDestination(state, { destination: name }));
  }

  get destination(): string { return this.destinationName; }

  constructor(private store: Store) { }
}
