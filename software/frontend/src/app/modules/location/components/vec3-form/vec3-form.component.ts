import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'ucm-vec3-form',
  templateUrl: './vec3-form.component.html',
  styleUrls: ['./vec3-form.component.css']
})
export class Vec3FormComponent {
  @Input() name: string | undefined;

  @Input() editable = true;

  @Input()
  set vec3(vec3: number[] | undefined) {
    if (vec3) {
      this.vec3Form.setValue({
        x: vec3[0],
        y: vec3[1],
        z: vec3[2]
      });
    }
  }

  @Output() vec3Change = new EventEmitter<number[]>();

  @Output() editingChange = new EventEmitter<boolean>();

  @Output() saveChanges = new EventEmitter<void>();

  @Output() revertChanges = new EventEmitter<void>();

  vec3Form = new FormGroup({
    x: new FormControl(0.0, Validators.required),
    y: new FormControl(0.0, Validators.required),
    z: new FormControl(0.0, Validators.required)
  });

  editing = false;

  constructor() {
    this.vec3Form.valueChanges.subscribe((value) => {
      this.vec3Change.emit([value.x, value.y, value.z]);
    });
  }

  onStartEditing(): void {
    this.editing = true;
    this.editingChange.emit(this.editing);
  }

  onSaveChanges(): void {
    this.editing = false;
    this.editingChange.emit(this.editing);
    this.saveChanges.emit();
  }

  onRevertChanges(): void {
    this.editing = false;
    this.editingChange.emit(this.editing);
    this.revertChanges.emit();
  }
}
