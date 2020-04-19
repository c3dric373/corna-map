import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-map-menu',
  templateUrl: './map-menu.component.html',
  styleUrls: ['./map-menu.component.css']
})
export class MapMenuComponent implements OnInit {
  public location: string;
  public isRegion: boolean;

  constructor() {}

  ngOnInit(): void {
  }

  clickOnLocation(location: string) {
    this.location = location;
  }

  setIsRegion(isRegion: boolean) {
    this.isRegion = isRegion;
  }
}
