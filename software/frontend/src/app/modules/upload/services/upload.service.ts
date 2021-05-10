import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UploadModel } from '../models/upload.model';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UploadService {
  constructor(private httpClient: HttpClient) {}

  upload(file: File): Observable<UploadModel> {
    const formData = new FormData();
    formData.append('file', file, file.name);
    return this.httpClient.post<UploadModel>(`${environment.backendUrl}/upload`, formData);
  }
}
