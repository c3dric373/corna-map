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
  m0_14: boolean;
  m15_44: boolean;
  m45_64: boolean;
  m65_74: boolean;
  m75: boolean;

  constructor() {
    this.m0_14 = false;
    this.m15_44 = false;
    this.m45_64 = false;
    this.m65_74 = false;
    this.m75 = false;
  }
}

export class ConfCategory {
  c0_14: boolean;
  c15_44: boolean;
  c45_64: boolean;
  c65_74: boolean;
  c75: boolean;

  constructor() {
    this.c0_14 = false;
    this.c15_44 = false;
    this.c45_64 = false;
    this.c65_74 = false;
    this.c75 = false;
  }
}
