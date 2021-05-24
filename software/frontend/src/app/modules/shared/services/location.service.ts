import { Injectable } from '@angular/core';
import { EntityCollectionServiceBase, EntityCollectionServiceElementsFactory } from '@ngrx/data';
import { LocationModel } from '../models/location.model';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { MeasurementsTimeframe } from '../../location/models/measurements-timeframe';
import { MeasurementsType } from '../../location/models/measurements-type';
import { LocationMeasurementsModel } from '../models/location-measurements.model';

@Injectable({
  providedIn: 'root'
})
export class LocationService extends EntityCollectionServiceBase<LocationModel> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory, private httpClient: HttpClient) {
    super('Location', serviceElementsFactory);
  }

  private static buildGetMeasurementsRequest(measurementsType: MeasurementsType, timeframe: MeasurementsTimeframe): any {
    let from: Date;
    let to: Date;
    let resolution: 'minutes' | 'hours' | 'days' | 'weeks';

    const currentDate = new Date();

    switch (timeframe) {
      case MeasurementsTimeframe.LAST_6_HOURS:
        const currentDateMinus6Hours = new Date();
        currentDateMinus6Hours.setHours(currentDateMinus6Hours.getHours() - 6);
        resolution = 'minutes';
        from = currentDateMinus6Hours;
        to = currentDate;
        break;
      case MeasurementsTimeframe.LAST_WEEK:
        const currentDateMinus1Week = new Date();
        currentDateMinus1Week.setDate(currentDateMinus1Week.getDate() - 7);
        resolution = 'hours';
        from = currentDateMinus1Week;
        to = currentDate;
        break;
      case MeasurementsTimeframe.LAST_MONTH:
        const currentDateMinus1Month = new Date();
        currentDateMinus1Month.setDate(currentDateMinus1Month.getDate() - 30);
        resolution = 'days';
        from = currentDateMinus1Month;
        to = currentDate;
        break;
      case MeasurementsTimeframe.LAST_YEAR:
        const currentDateMinus1Year = new Date();
        currentDateMinus1Year.setDate(currentDateMinus1Year.getDate() - 365);
        resolution = 'weeks';
        from = currentDateMinus1Year;
        to = currentDate;
        break;
    }

    const request = {
      from,
      to,
      type: ((measurementsType as unknown) as string).toLowerCase(),
      resolution
    };

    return request;
  }

  loadMeasurements(locationId: number, type: MeasurementsType, timeframe: MeasurementsTimeframe): Observable<LocationMeasurementsModel> {
    return this.httpClient.post<LocationMeasurementsModel>(
      `${environment.backendUrl}/location/${locationId}/measurements`,
      LocationService.buildGetMeasurementsRequest(type, timeframe)
    );
  }
}
