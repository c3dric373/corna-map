import { Component, OnInit } from '@angular/core';
import {MapService} from '../../service/map/map.service';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {
  result = 'rien';
  color = 'blue';
  tab = ['Rhône', 'Ille-et-Vilaine', 'Aude'];

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

  essaiCouleur() {
    this.color = 'yell';
    for (const mot in this.tab) {
      let word = this.tab[mot];
      console.log(word);

      const depts = document.getElementsByTagName('path'); // document.getElementsByTagName('path')[0].style.fill = 'darkblue';
      for (const dep in depts) {
        const depElement = depts[dep];
        const attribut = depElement.attributes;
        if (attribut) {
          const nomDep = attribut.getNamedItem('data-nom');
          /*if( nomDep.toString() === word) {
            console.log(nomDep);
            depElement..style.fill = 'darkblue';
          }*/
        }
      }
    }
  }

}
