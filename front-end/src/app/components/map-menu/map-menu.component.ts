import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-map-menu',
  templateUrl: './map-menu.component.html',
  styleUrls: ['./map-menu.component.css']
})
export class MapMenuComponent implements OnInit {
  public location: string;
  public isRegion: boolean;
  public loading: boolean;

  constructor() {}

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
}
