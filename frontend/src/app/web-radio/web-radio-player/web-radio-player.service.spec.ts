import { TestBed } from '@angular/core/testing';

import { WebRadioPlayerService } from './web-radio-player.service';

describe('WebRadioPlayerService', () => {
  let service: WebRadioPlayerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WebRadioPlayerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
