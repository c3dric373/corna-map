import {NgbDate} from '@ng-bootstrap/ng-bootstrap';

export class SimulParams {
  reset: boolean;
  borders: boolean;
  shops: boolean;
  hosp: boolean;
  respectConfinement: number;
  mask: MaskCategory;
  conf: ConfCategory;
  startDate: NgbDate;
  endDate: NgbDate;

  constructor() {
    this.respectConfinement = 50;
    this.mask = new MaskCategory();
    this.conf = new ConfCategory();
  }
}

export class MaskCategory {
  m0_15: boolean;
  m16_19: boolean;
  m30_49: boolean;
  m50_69: boolean;
  m70: boolean;

  constructor() {
    this.m0_15 = false;
    this.m16_19 = false;
    this.m30_49 = false;
    this.m50_69 = false;
    this.m70 = false;
  }
}

export class ConfCategory {
  c0_15: boolean;
  c16_19: boolean;
  c30_49: boolean;
  c50_69: boolean;
  c70: boolean;

  constructor() {
    this.c0_15 = false;
    this.c16_19 = false;
    this.c30_49 = false;
    this.c50_69 = false;
    this.c70 = false;
  }
}
