import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { LocationModel } from '../../../shared/models/location.model';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { LocationService } from '../../../shared/services/location.service';

@Component({
  selector: 'ucm-location-modal',
  templateUrl: './location-modal.component.html',
  styleUrls: ['./location-modal.component.css']
})
export class LocationModalComponent implements OnInit {
  locationForm = new FormGroup({
    name: new FormControl('', Validators.required),
    icon: new FormControl(null),
    model3d: new FormControl(null)
  });

  location: LocationModel | undefined;

  constructor(public modalRef: BsModalRef, private locationService: LocationService, private cd: ChangeDetectorRef) {}

  switchOnModelType(addKey: string, editKey: string): string {
    if (this.location !== undefined) {
      return editKey;
    }
    return addKey;
  }

  ngOnInit(): void {
    if (this.location) {
      this.locationService.getByKey(this.location.id).subscribe((location) => {
        this.location = location;
        this.locationForm.setValue(location);
      });
    }
  }

  onSubmit(): void {
    if (this.location) {
      this.locationService.update(this.locationForm.value).subscribe(() => {
        this.modalRef.hide();
      });
    } else {
      this.locationService.add(this.locationForm.value).subscribe(() => {
        this.modalRef.hide();
      });
    }
  }
}
