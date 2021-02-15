import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StationSearchComponent } from './station-search.component';

describe('StationSearchComponent', () => {
  let component: StationSearchComponent;
  let fixture: ComponentFixture<StationSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StationSearchComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StationSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
