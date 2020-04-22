import { TestBed } from '@angular/core/testing';

import { HistoriqueService } from './historique.service';
import {HttpClientModule} from '@angular/common/http';

describe('HistoriqueService', () => {
  let service: HistoriqueService;

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [HttpClientModule]});
    service = TestBed.inject(HistoriqueService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
