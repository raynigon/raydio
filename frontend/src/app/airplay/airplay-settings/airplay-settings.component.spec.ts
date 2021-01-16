import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AirplaySettingsComponent } from './airplay-settings.component';

describe('AirplaySettingsComponent', () => {
  let component: AirplaySettingsComponent;
  let fixture: ComponentFixture<AirplaySettingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AirplaySettingsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AirplaySettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
