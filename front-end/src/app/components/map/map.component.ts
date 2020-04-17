import { Component, OnInit } from '@angular/core';
import {MapService} from '../../service/map/map.service';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {
  isRegion;
  tabColor = [ '#f4a582', '#d6604d', '#b2182b'];
  result;

  tabtab = [
  'Guadeloupe' ,
  'Martinique' ,
  'Guyane' ,
  'La Réunion' ,
  'Mayotte' ,
  'Paris' ,
  'Seine-et-Marne' ,
  'Yvelines' ,
  'Essonne' ,
  'Hauts-de-Seine' ,
  'Seine-Saint-Denis' ,
  'Val-de-Marne' ,
  'Val-d’Oise' ,
  'Cher' ,
  'Eure-et-Loir' ,
  'Indre' ,
  'Indre-et-Loire' ,
  'Loir-et-Cher' ,
  'Loiret' ,
  'Côte-d’Or' ,
  'Doubs' ,
  'Jura' ,
  'Nièvre' ,
  'Haute-Saône' ,
  'Saône-et-Loire' ,
  'Yonne' ,
  'Territoire de Belfort' ,
  'Calvados' ,
  'Eure' ,
  'Manche' ,
  'Orne' ,
  'Seine-Maritime' ,
  'Aisne' ,
  'Nord' ,
  'Oise' ,
  'Pas-de-Calais' ,
  'Somme' ,
  'Ardennes' ,
  'Aube' ,
  'Marne' ,
  'Haute-Marne' ,
  'Meurthe-et-Moselle' ,
  'Meuse' ,
  'Moselle' ,
  'Bas-Rhin' ,
  'Haut-Rhin' ,
  'Vosges' ,
  'Loire-Atlantique' ,
  'Maine-et-Loire' ,
  'Mayenne' ,
  'Sarthe' ,
  'Vendée' ,
  'Côtes-d’Armor' ,
  'Finistère' ,
  'Ille-et-Vilaine' ,
  'Morbihan' ,
  'Charente' ,
  'Charente-Maritime' ,
  'Corrèze' ,
  'Creuse' ,
  'Dordogne' ,
  'Gironde' ,
  'Landes' ,
  'Lot-et-Garonne' ,
  'Pyrénées-Atlantiques' ,
  'Deux-Sèvres' ,
  'Vienne' ,
  'Haute-Vienne' ,
  'Ariège' ,
  'Aude' ,
  'Aveyron' ,
  'Gard' ,
  'Haute-Garonne' ,
  'Gers' ,
  'Hérault' ,
  'Lot' ,
  'Lozère' ,
  'Hautes-Pyrénées' ,
  'Pyrénées-Orientales' ,
  'Tarn' ,
  'Tarn-et-Garonne' ,
  'Ain' ,
  'Allier' ,
  'Ardèche' ,
  'Cantal' ,
  'Drôme' ,
  'Isère' ,
  'Loire' ,
  'Haute-Loire' ,
  'Puy-de-Dôme' ,
  'Rhône' ,
  'Savoie' ,
  'Haute-Savoie' ,
  'Alpes-de-Haute-Provence' ,
  'Hautes-Alpes' ,
  'Alpes-Maritimes' ,
  'Bouches-du-Rhône' ,
  'Var' ,
  'Vaucluse' ,
  'Corse-du-Sud' ,
  'Haute-Corse' ,
  'Guadeloupe' ,
  'Martinique' ,
  'Guyane' ,
  'La Réunion' ,
  'Mayotte' ,
  'Paris' ,
  'Seine-et-Marne' ,
  'Yvelines' ,
  'Essonne' ,
  'Hauts-de-Seine' ,
  'Seine-Saint-Denis' ,
  'Val-de-Marne' ,
  'Val-d’Oise' ,
  'Cher' ,
  'Eure-et-Loir' ,
  'Indre' ,
  'Indre-et-Loire' ,
  'Loir-et-Cher' ,
  'Loiret' ,
  'Côte-d’Or' ,
  'Doubs' ,
  'Jura' ,
  'Nièvre' ,
  'Haute-Saône' ,
  'Saône-et-Loire' ,
  'Yonne' ,
  'Territoire de Belfort' ,
  'Calvados' ,
  'Eure' ,
  'Manche' ,
  'Orne' ,
  'Seine-Maritime' ,
  'Aisne' ,
  'Nord' ,
  'Oise' ,
  'Pas-de-Calais' ,
  'Somme' ,
  'Ardennes' ,
  'Aube' ,
  'Marne' ,
  'Haute-Marne' ,
  'Meurthe-et-Moselle' ,
  'Meuse' ,
  'Moselle' ,
  'Bas-Rhin' ,
  'Haut-Rhin' ,
  'Vosges' ,
  'Loire-Atlantique' ,
  'Maine-et-Loire' ,
  'Mayenne' ,
  'Sarthe' ,
  'Vendée' ,
  'Côtes-d’Armor' ,
  'Finistère' ,
  'Ille-et-Vilaine' ,
  'Morbihan' ,
  'Charente' ,
  'Charente-Maritime' ,
  'Corrèze' ,
  'Creuse' ,
  'Dordogne' ,
  'Gironde' ,
  'Landes' ,
  'Lot-et-Garonne' ,
  'Pyrénées-Atlantiques' ,
  'Deux-Sèvres' ,
  'Vienne' ,
  'Haute-Vienne' ,
  'Ariège' ,
  'Aude' ,
  'Aveyron' ,
  'Gard' ,
  'Haute-Garonne' ,
  'Gers' ,
  'Hérault' ,
  'Lot' ,
  'Lozère' ,
  'Hautes-Pyrénées' ,
  'Pyrénées-Orientales' ,
  'Tarn' ,
  'Tarn-et-Garonne' ,
  'Ain' ,
  'Allier' ,
  'Ardèche' ,
  'Cantal' ,
  'Drôme' ,
  'Isère' ,
  'Loire' ,
  'Haute-Loire' ,
  'Puy-de-Dôme' ,
  'Rhône' ,
  'Savoie' ,
  'Haute-Savoie' ,
  'Alpes-de-Haute-Provence' ,
  'Hautes-Alpes' ,
  'Alpes-Maritimes' ,
  'Bouches-du-Rhône' ,
  'Var' ,
  'Vaucluse' ,
  'Corse-du-Sud' ,
  'Haute-Corse' ,
  'Guadeloupe' ,
  'Martinique' ,
  'Guyane' ,
  'La Réunion' ,
  'Mayotte' ,
  'Paris' ,
  'Seine-et-Marne' ,
  'Yvelines' ,
  'Essonne' ,
  'Hauts-de-Seine' ,
  'Seine-Saint-Denis' ,
  'Val-de-Marne' ,
  'Val-d’Oise' ,
  'Cher' ,
  'Eure-et-Loir' ,
  'Indre' ,
  'Indre-et-Loire' ,
  'Loir-et-Cher' ,
  'Loiret' ,
  'Côte-d’Or' ,
  'Doubs' ,
  'Jura' ,
  'Nièvre' ,
  'Haute-Saône' ,
  'Saône-et-Loire' ,
  'Yonne' ,
  'Territoire de Belfort' ,
  'Calvados' ,
  'Eure' ,
  'Manche' ,
  'Orne' ,
  'Seine-Maritime' ,
  'Aisne' ,
  'Nord' ,
  'Oise' ,
  'Pas-de-Calais' ,
  'Somme' ,
  'Ardennes' ,
  'Aube' ,
  'Marne' ,
  'Haute-Marne' ,
  'Meurthe-et-Moselle' ,
  'Meuse' ,
  'Moselle' ,
  'Bas-Rhin' ,
  'Haut-Rhin' ,
  'Vosges' ,
  'Loire-Atlantique' ,
  'Maine-et-Loire' ,
  'Mayenne' ,
  'Sarthe' ,
  'Vendée' ,
  'Côtes-d’Armor' ,
  'Finistère',
  'Ille-et-Vilaine' ,
  'Morbihan' ,
  'Charente' ,
  'Charente-Maritime' ,
  'Corrèze' ,
  'Creuse' ,
  'Dordogne' ,
  'Gironde' ,
  'Landes' ,
  'Lot-et-Garonne' ,
  'Pyrénées-Atlantiques' ,
  'Deux-Sèvres' ,
  'Vienne' ,
  'Haute-Vienne' ,
  'Ariège',
  'Aude' ,
  'Aveyron' ,
  'Gard' ,
  'Haute-Garonne' ,
  'Gers' ,
  'Hérault' ,
  'Lot' ,
  'Lozère' ,
  'Hautes-Pyrénées' ,
  'Pyrénées-Orientales' ,
  'Tarn' ,
  'Tarn-et-Garonne' ,
  'Ain' ,
  'Allier' ,
  'Ardèche' ,
  'Cantal' ,
  'Drôme' ,
  'Isère' ,
  'Loire' ,
  'Haute-Loire' ,
  'Puy-de-Dôme' ,
  'Rhône' ,
  'Savoie' ,
  'Haute-Savoie' ,
  'Alpes-de-Haute-Provence' ,
  'Hautes-Alpes' ,
  'Alpes-Maritimes' ,
  'Bouches-du-Rhône' ,
  'Var' ,
  'Vaucluse' ,
  'Corse-du-Sud' ,
  'Haute-Corse' ];

  tabReg = [
    'Guadeloupe',
  'Martinique',
  'Guyane',
  'La Réunion',
  'Mayotte',
  'Île-de-France',
  'Centre-Val de Loire',
  'Bourgogne-Franche-Comté',
  'Normandie',
  'Hauts-de-France',
  'Grand Est',
  'Pays de la Loire',
  'Nouvelle-Aquitaine',
  'Occitanie',
  'Auvergne-Rhône-Alpes',
  'Provence-Alpes-Côte d\'Azur',
  'Bretagne',
  'Corse'
  ];
  mousOverReg = new Object();
  mousLeaveReg = new Object();

  constructor(private mapService: MapService) {}

  ngOnInit(): void {
    this.isRegion = true;
    this.initializeMapReg();
  }

  changeScale(): void {
    this.isRegion = !this.isRegion;
    console.log(this.isRegion);
    if (this.isRegion){
      this.initializeMapReg();
    } else{
       this.removeRegListener();
       this.initializeMapDept();
    }
  }

  essaiMap() {
    this.mapService.getMap().subscribe(
      data => {
        this.result = data;
        console.log(data);
      }
    );
  }

  initializeMapDept() {
    for (const index in this.tabtab) {
      const depName = this.tabtab[index];
      const color = this.assignColor(index);

      // Get the elements starting with <path
      const pathElements = document.getElementsByTagName('path');
      for (const element in pathElements) {
        const depElement = pathElements[element];
        const elementAttributes = depElement.attributes;

        // If the element has attributes and
        // If the element name is 'data-nom'
        // Color the element with the corresponding color
        if (elementAttributes) {
          const dataNom = elementAttributes.getNamedItem('data-nom');
          if ( dataNom && dataNom.value === depName) {
            depElement.addEventListener('mouseover',
              function(){
                        depElement.style.fillOpacity = '0.7';
                        depElement.style.stroke = 'white';
                        depElement.style.strokeOpacity = '0.8';
                      } );
            depElement.addEventListener('mouseleave',
              function(){
                        depElement.style.fillOpacity = '1';
                        depElement.style.stroke = color;
                      } );
            depElement.style.fill = color;
          }
        }
      }
    }
  }

  initializeMapReg() {
    for (const index in this.tabReg) {
      const RegName = this.tabReg[index];
      const color = this.assignColor(index);

      // Get the elements of regions
      const pathElements = document.getElementsByClassName('region');
      for (const element in pathElements) {
        const regElement = pathElements[element] as HTMLElement;
        const elementAttributes = regElement.attributes;

        // If the element has attributes and
        // If the element name is 'data-nom'
        // Color the departements of the element with the corresponding color
        if (elementAttributes) {
          const dataNom = elementAttributes.getNamedItem('data-nom');
          if ( dataNom && dataNom.value === RegName) {
            let mousOver = function() {
              for (let a = 0; a < regElement.children.length; a++) {
                const reDept = regElement.children[a] as HTMLElement;
                reDept.style.fillOpacity = '0.7';
                reDept.style.strokeOpacity = '0.1';
              }
            };
            regElement.addEventListener('mouseover', mousOver);
            this.mousOverReg[dataNom.value] = mousOver;

            let mousLeave =function() {
              for (let a = 0; a < regElement.children.length; a++) {
                const reDept = regElement.children[a] as HTMLElement;
                reDept.style.fillOpacity = '1';
                reDept.style.strokeOpacity = '0.6';
              }
            };
            regElement.addEventListener('mouseleave', mousLeave);
            this.mousLeaveReg[dataNom.value] = mousLeave;

            for ( let i = 0; i < regElement.children.length ; i ++) {
              const regDept = regElement.children[i]  as HTMLElement;
              regDept.style.fill = color;
              regDept.style.stroke = color;
              regDept.style.strokeOpacity = '0.6';
              }
          }
        }
      }
    }
  }

  removeRegListener(): void {
    const pathElements = document.getElementsByClassName('region');
    for (const element in pathElements) {
      const regElement = pathElements[element] as HTMLElement;
      const elementAttributes = regElement.attributes;

      // If the element has attributes and
      // If the element name is 'data-nom'
      // Color the departements of the element with the corresponding color
      if (elementAttributes) {
        const dataNom = elementAttributes.getNamedItem('data-nom');
        if (dataNom) {
          regElement.removeEventListener('mouseover', this.mousOverReg[dataNom.value]);
          regElement.removeEventListener('mouseleave', this.mousLeaveReg[dataNom.value]);
        }
      }
    }
    this.mousOverReg = new Object();
    this.mousLeaveReg = new Object();
  }

  assignColor(nb){
    const color = this.tabColor[nb % 3];
    return color;
  }

}
