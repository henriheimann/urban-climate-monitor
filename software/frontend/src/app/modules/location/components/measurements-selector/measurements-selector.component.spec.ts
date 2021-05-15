import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MeasurementsSelectorComponent } from './measurements-selector.component';

describe('MeasurementsSelectorComponent', () => {
  let component: MeasurementsSelectorComponent;
  let fixture: ComponentFixture<MeasurementsSelectorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MeasurementsSelectorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MeasurementsSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
