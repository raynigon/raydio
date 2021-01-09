import { TestBed } from '@angular/core/testing';

import { WebRadioRepositoryService } from './web-radio-repository.service';

describe('WebRadioRepositoryService', () => {
  let service: WebRadioRepositoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WebRadioRepositoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
