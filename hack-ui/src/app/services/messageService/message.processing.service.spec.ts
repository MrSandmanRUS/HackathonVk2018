import { TestBed, inject } from '@angular/core/testing';

import { MessageProcessingService } from './message.processing.service';

describe('Message.ProcessingService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MessageProcessingService]
    });
  });

  it('should be created', inject([MessageProcessingService], (service: MessageProcessingService) => {
    expect(service).toBeTruthy();
  }));
});
