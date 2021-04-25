import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HexOutputComponent } from './hex-output.component';

describe('HexOutputComponent', () => {
  let component: HexOutputComponent;
  let fixture: ComponentFixture<HexOutputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HexOutputComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HexOutputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
