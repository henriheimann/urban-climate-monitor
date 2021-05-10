import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FileUploadComponent } from './components/file-upload/file-upload.component';

@NgModule({
  declarations: [FileUploadComponent],
  exports: [FileUploadComponent],
  imports: [CommonModule]
})
export class UploadModule {}
