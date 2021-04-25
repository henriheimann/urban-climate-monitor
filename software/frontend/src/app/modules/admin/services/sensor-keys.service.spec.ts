import { TestBed } from '@angular/core/testing';

import { SensorKeysService } from './sensor-keys.service';

describe('SensorKeysService', () => {
  let service: SensorKeysService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SensorKeysService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
