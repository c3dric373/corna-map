import { Component, OnInit } from '@angular/core';
import {MapService} from '../../service/map/map.service';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css']
})
export class SideBarComponent implements OnInit {

  constructor(private mapService: MapService) {}

  nbCC = 548;
  nbG = 444;
  nbH = 128;
  nbD = 185;

  donnees ;

  ngOnInit(): void {
    this.essaiMap();
  }

  essaiMap() {
    this.mapService.getMap().subscribe(
      data => {
        this.donnees = data;
        console.log(data);
      }
    );
}

  essaiMapRegion() {
    this.mapService.getMapRegion().subscribe(
      data => {
        this.donnees = data;
        console.log(data);
      }
    );


  }


}
