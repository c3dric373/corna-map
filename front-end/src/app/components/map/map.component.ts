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
  'Bretagne',
  'Nouvelle-Aquitaine',
  'Occitanie',
  'Auvergne-Rhône-Alpes',
  'Provence-Alpes-Côte d\'Azur',
  'Corse',
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
  'Bretagne',
  'Nouvelle-Aquitaine',
  'Occitanie',
  'Auvergne-Rhône-Alpes',
  'Provence-Alpes-Côte d\'Azur',
  'Corse'
  ];

  constructor(private mapService: MapService) {}

  ngOnInit(): void {
    this.isRegion = true;
    this.essaiCouleurReg();
  }

  essaiMap() {
    this.mapService.getMap().subscribe(
      data => {
        this.result = data;
        console.log(data);
      }
    );
  }

  essaiCouleurDept() {
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
          if ( dataNom.value === depName) {
            depElement.addEventListener('mouseover',  function(){ depElement.style.fill = 'gold'; } );
            depElement.addEventListener('mouseleave',  function(){ depElement.style.fill = color; } );
            depElement.style.fill = color;
            console.log(depElement);
          }
        }
      }
    }
  }

  essaiCouleurReg() {
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
          if ( dataNom.value === RegName) {
              for ( let i = 0; i < regElement.children.length ; i ++) {
                const regDept = regElement.children[i]  as HTMLElement;
                regDept.addEventListener('mouseover',
                  function() {
                              for (let a = 0; a < regElement.children.length; a++) {
                                const reDept = regElement.children[a] as HTMLElement;
                                reDept.style.fillOpacity = '0.7';
                                reDept.style.strokeOpacity = '0.1';
                              }
                            });
                regDept.addEventListener('mouseleave',
                  function(){
                            for (let a = 0; a < regElement.children.length; a++) {
                              const reDept = regElement.children[a] as HTMLElement;
                              reDept.style.fillOpacity = '1';
                              reDept.style.strokeOpacity = '0.6';
                            }
                          });
                regDept.style.fill = color;
                regDept.style.stroke = color;
                regDept.style.strokeOpacity = '0.6';
                }
          }
        }
      }
    }
  }

  assignColor(nb){
    const color = this.tabColor[nb % 3];
    return color;
  }

}
