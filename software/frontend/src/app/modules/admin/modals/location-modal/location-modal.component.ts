import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Location} from '../../../shared/models/location.model';
import {EntityCollectionService, EntityCollectionServiceFactory} from '@ngrx/data';
import {BsModalRef} from 'ngx-bootstrap/modal';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {User} from '../../../shared/models/user.model';

@Component({
  selector: 'ucm-location-modal',
  templateUrl: './location-modal.component.html',
  styleUrls: ['./location-modal.component.css']
})
export class LocationModalComponent implements OnInit {

  locationForm = new FormGroup({
    name: new FormControl('', Validators.required),
    icon: new FormControl(null, Validators.required),
    model3d: new FormControl(null, Validators.required)
  });

  locationService: EntityCollectionService<Location>;

  constructor(public modalRef: BsModalRef, EntityCollectionServiceFactoryClass: EntityCollectionServiceFactory,
              private cd: ChangeDetectorRef) {
    this.locationService = EntityCollectionServiceFactoryClass.create<Location>('Location');
  }

  location: Location | undefined;

  switchOnModelType(addKey: string, editKey: string): string {
    if (this.location !== undefined) {
      return editKey;
    } else {
      return addKey;
    }
  }

  ngOnInit(): void {
    if (this.location) {
      this.locationForm.setValue({
        name: this.location.name,
        icon: this.location.icon,
        model3d: this.location.model3d
      });
    }
  }

  onSubmit(): void {
    if (this.location) {
      this.locationService.update({
        id: this.location.id,
        name: this.locationForm.get('name')?.value,
        icon: this.locationForm.get('icon')?.value,
        model3d: this.locationForm.get('model3d')?.value
      }).subscribe(() => {
        this.modalRef.hide();
      });
    } else {
      this.locationService.add({
        id: null,
        name: this.locationForm.get('name')?.value,
        icon: this.locationForm.get('icon')?.value,
        model3d: this.locationForm.get('model3d')?.value
      }).subscribe(() => {
        this.modalRef.hide();
      });
    }
  }

  onFileChange(controlName: string, $event: Event): void {
    const reader = new FileReader();

    const input = $event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      reader.readAsDataURL(file);

      reader.onload = () => {
        const stringResult = reader.result as string;

        this.locationForm.patchValue({
          [controlName]: {
            filename: file.name,
            data: stringResult.substring(stringResult.indexOf(',') + 1)
          }
        });

        // need to run CD since file load runs outside of zone
        this.cd.markForCheck();
      };
    }
  }
}
