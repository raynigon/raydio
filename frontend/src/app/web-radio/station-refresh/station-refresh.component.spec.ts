import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StationRefreshComponent } from './station-refresh.component';

describe('StationRefreshComponent', () => {
  let component: StationRefreshComponent;
  let fixture: ComponentFixture<StationRefreshComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StationRefreshComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StationRefreshComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
