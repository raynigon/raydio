import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WebRadioSettingsComponent } from './web-radio-settings.component';

describe('WebRadioSettingsComponent', () => {
  let component: WebRadioSettingsComponent;
  let fixture: ComponentFixture<WebRadioSettingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WebRadioSettingsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WebRadioSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
