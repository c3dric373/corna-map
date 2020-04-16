import { Component, OnInit } from '@angular/core';
import {MapService} from '../../service/map/map.service';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {
  result = 'rien';

  constructor(private mapService: MapService) {}

  ngOnInit(): void {
  }

  essaiMap() {
    this.mapService.getMap().subscribe(
      data => {
        console.log(JSON.parse(data));
      }
    );
  }
}
