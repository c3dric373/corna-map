import {NgbDate} from '@ng-bootstrap/ng-bootstrap';

export class InfosFrance {
  id: string;
  nom: string;
  criticalCases: number;
  hospitalized: number;
  totalCases: number;
  ephadCases: number;
  ephadConfirmedCases: number;
  ephadPossibleCases: number;
  totalDeaths: number;
  totalEphadDeaths: number;
  recoveredCases: number;
  totalTests: number;
  date: NgbDate;
  type: string;

  constructor() {
    this.criticalCases = 0;
    this.hospitalized = 0;
    this.totalCases = 0;
    this.totalDeaths = 0;
    this.recoveredCases = 0;
  }
}
