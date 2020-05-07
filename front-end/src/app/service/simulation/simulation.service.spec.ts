import { TestBed } from '@angular/core/testing';

import { SimulationService } from './simulation.service';
import {HttpClientModule} from '@angular/common/http';

describe('SimulationService', () => {
  let service: SimulationService;

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [HttpClientModule]});
    service = TestBed.inject(SimulationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
