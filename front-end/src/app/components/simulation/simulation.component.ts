import { Component, OnInit } from '@angular/core';
import {NgbCalendar, NgbDate} from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-simulation',
  templateUrl: './simulation.component.html',
  styleUrls: ['./simulation.component.css']
})
export class SimulationComponent implements OnInit {
  public location: string;
  public isRegion: boolean;
  public loading: boolean;
  public isOnlyMap = false;
  public date: NgbDate;
  public type = 'simulation';
  public isSimulStarted: boolean;

  constructor(private calendar: NgbCalendar) {
    this.date =  new NgbDate(2020, 3, 18);
    this.isSimulStarted = false;
  }

  ngOnInit(): void {
    this.loading = true;
  }

  clickOnLocation(location: string) {
    this.location = location;
  }

  setIsRegion(isRegion: boolean) {
    this.isRegion = isRegion;
  }

  setLoading(loading: boolean) {
    this.loading = loading;
  }

  setOnlyMap(isMap: boolean) {
    this.isOnlyMap = isMap;
  }

  setDate(actualDate: NgbDate) {
    this.date = actualDate;
  }

  setSimulState(isStarted: boolean) {
    this.isSimulStarted = isStarted;
  }

}
