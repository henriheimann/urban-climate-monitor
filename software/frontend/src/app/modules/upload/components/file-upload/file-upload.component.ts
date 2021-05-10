import { Component, Input } from '@angular/core';
import { AbstractControl, ControlValueAccessor, NG_VALIDATORS, NG_VALUE_ACCESSOR, ValidationErrors, Validator } from '@angular/forms';
import { UploadService } from '../../services/upload.service';
import { UploadModel } from '../../models/upload.model';

@Component({
  selector: 'ucm-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      multi: true,
      useExisting: FileUploadComponent
    },
    {
      provide: NG_VALIDATORS,
      multi: true,
      useExisting: FileUploadComponent
    }
  ]
})
export class FileUploadComponent implements ControlValueAccessor, Validator {
  touched = false;
  disabled = false;

  uploading = false;
  fileUpload: UploadModel | null = null;

  @Input()
  accept = '';

  @Input()
  removeText = 'Remove';

  @Input()
  uploadingText = 'Uploading...';

  @Input()
  uploadedFileText = 'Uploaded File: ';

  constructor(private fileUploadService: UploadService) {}

  onChange: (fileUpload: UploadModel | null) => unknown = () => {};

  onTouched: () => unknown = () => {};

  registerOnChange(onChange: (fileUpload: UploadModel | null) => unknown): void {
    this.onChange = onChange;
  }

  registerOnTouched(onTouched: () => unknown): void {
    this.onTouched = onTouched;
  }

  writeValue(fileUpload: UploadModel): void {
    this.fileUpload = fileUpload;
  }

  setDisabledState(disabled: boolean): void {
    this.disabled = disabled;
  }

  markAsTouched(): void {
    if (!this.touched) {
      this.onTouched();
      this.touched = true;
    }
  }

  onFileChange($event: Event): void {
    this.markAsTouched();
    if (this.disabled) {
      return;
    }

    const input = $event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      const file = input.files[0];

      this.uploading = true;

      this.fileUploadService.upload(file).subscribe(
        (fileUpload) => {
          this.fileUpload = fileUpload;
          this.uploading = false;
          this.onChange(this.fileUpload);
        },
        () => {
          this.fileUpload = null;
          this.uploading = false;
          this.onChange(this.fileUpload);
        }
      );
    }
  }

  onRemove(): void {
    this.markAsTouched();
    if (this.disabled) {
      return;
    }

    this.fileUpload = null;
    this.onChange(this.fileUpload);
  }

  validate(control: AbstractControl): ValidationErrors | null {
    const fileUpload = control.value;
    if (fileUpload == null) {
      return {
        noFileUploaded: null
      };
    } else {
      return null;
    }
  }
}
