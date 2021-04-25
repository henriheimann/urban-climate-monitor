import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../../environments/environment';
import {SensorKeys} from '../models/sensor-keys.model';

@Injectable({
  providedIn: 'root'
})
export class SensorKeysService {

  constructor(private httpClient: HttpClient) { }

  getSensorKeys(sensorId: number): Observable<SensorKeys> {
    return this.httpClient.get<SensorKeys>(`${environment.backendUrl}/sensor/${sensorId}/keys`);
  }
}
