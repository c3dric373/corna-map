import { Component, OnInit } from '@angular/core';
import {NgbDate} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-map-menu',
  templateUrl: './map-menu.component.html',
  styleUrls: ['./map-menu.component.css']
})
export class MapMenuComponent implements OnInit {
  public location: string;
  public isRegion: boolean;
  public loading: boolean;
  public isOnlyMap = false;
  public date: NgbDate;
  public type = 'map';

  constructor() {}

  ngOnInit(): void {
    this.loading = true;
    this.location = 'France';
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
}
