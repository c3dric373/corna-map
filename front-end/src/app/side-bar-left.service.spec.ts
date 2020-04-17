import { TestBed } from '@angular/core/testing';

import { SideBarLeftService } from './side-bar-left.service';

describe('SideBarLeftService', () => {
  let service: SideBarLeftService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SideBarLeftService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
