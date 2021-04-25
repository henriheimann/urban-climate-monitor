import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SensorKeysModalComponent } from './sensor-keys-modal.component';

describe('SensorKeysModalComponent', () => {
  let component: SensorKeysModalComponent;
  let fixture: ComponentFixture<SensorKeysModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SensorKeysModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SensorKeysModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
