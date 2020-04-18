import { Component, OnInit } from '@angular/core';
import {MapService} from '../../service/map/map.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {
  isRegion: boolean;
  private tabColor = [ '#f4a582', '#d6604d', '#b2182b'];
  private mousOverReg = new Object();
  private mousLeaveReg = new Object();
  private mousOverDept = new Object();
  private mousLeaveDept = new Object();
  chosenLocation: string;
  private reglist;
  private deptList;
  loading: boolean;

  constructor(private mapService: MapService, private spinner: NgxSpinnerService) {}

  ngOnInit(): void {
    this.loading = true;
    this.isRegion = true;
    this.getRegInfos();
  }

  dispReg(): void {
    if(!this.isRegion) {
      this.isRegion = true;
      this.removeDeptListener();
      this.initializeMapReg();
    }
  }

  dispDept(): void {
    if(this.isRegion) {
      this.isRegion = false;
      this.removeRegListener();
      if(!this.deptList) {
        this.getRDeptInfos();
      } else {
        this.initializeMapDept();
      }
    }
  }

  getRegInfos() {
    this.mapService.getMapRegion().subscribe(
      data => {
        this.reglist = data;
        this.initializeMapReg();
      }
    );
  }

  getRDeptInfos() {
    this.mapService.getMapDept().subscribe(
      data => {
        this.deptList = data;
        console.log(data);
        this.initializeMapDept();
      }
    );
  }

  initializeMapDept() {
    for (const index in this.deptList) {
      const depName = this.deptList[index].nom;
      const nbHospitalized = this.deptList[index].hospitalized;
      const color = this.assignColor(nbHospitalized, this.deptList);

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
            let mousOver = function() {
              depElement.style.fillOpacity = '0.7';
              depElement.style.stroke = 'white';
              depElement.style.strokeOpacity = '0.8';
            };
            depElement.addEventListener('mouseover', mousOver );
            this.mousOverDept[dataNom.value] = mousOver;

            let mousLeave = function(){
              depElement.style.fillOpacity = '1';
              depElement.style.stroke = color;
            };
            depElement.addEventListener('mouseleave', mousLeave );
            this.mousLeaveDept[dataNom.value] = mousLeave;

            depElement.style.fill = color;
            depElement.style.fillOpacity = '1';
            depElement.style.stroke = color;
          }
        }
      }
    }
  }

  initializeMapReg() {
    for (const index in this.reglist) {
      const RegName = this.reglist[index].nom;
      const nbHospitalized = this.reglist[index].hospitalized;
      const color = this.assignColor(nbHospitalized, this.reglist);

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
          if (dataNom && dataNom.value === RegName) {
            let mousOver = function() {
              for (let a = 0; a < regElement.children.length; a++) {
                const reDept = regElement.children[a] as HTMLElement;
                reDept.style.fillOpacity = '0.7';
                reDept.style.strokeOpacity = '0.1';
              }
            };
            regElement.addEventListener('mouseover', mousOver);
            this.mousOverReg[dataNom.value] = mousOver;

            let mousLeave = function() {
              for (let a = 0; a < regElement.children.length; a++) {
                const reDept = regElement.children[a] as HTMLElement;
                reDept.style.fillOpacity = '1';
                reDept.style.strokeOpacity = '0.6';
              }
            };
            regElement.addEventListener('mouseleave', mousLeave);
            this.mousLeaveReg[dataNom.value] = mousLeave;

            for (let i = 0; i < regElement.children.length; i++) {
              const regDept = regElement.children[i] as HTMLElement;
              regDept.style.fill = color;
              regDept.style.stroke = color;
              regDept.style.strokeOpacity = '0.6';
            }
          }
        }
      }
    }
    this.loading = false;
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

  removeDeptListener(): void {
    const pathElements = document.getElementsByTagName('path');
    for (const element in pathElements) {
      const depElement = pathElements[element];
      const elementAttributes = depElement.attributes;

      if (elementAttributes) {
        const dataNom = elementAttributes.getNamedItem('data-nom');
        if (dataNom ) {
          depElement.removeEventListener('mouseover', this.mousOverDept[dataNom.value]);
          depElement.removeEventListener('mouseleave', this.mousLeaveDept[dataNom.value]);
        }
      }
    }
    this.mousOverDept = new Object();
    this.mousLeaveDept = new Object();
  }

  clickReg(region): void{
    if(this.isRegion) {
      this.chosenLocation = region;
    }
   }

  clickDept(departement): void{
    if ( !this.isRegion ) {
      this.chosenLocation = departement;
    }
   }

   assignColor(nb, list): string{
    const min = this.minNumber(list);
    const max = this.maxNumber(list);
    const diff = max - min;
    const categorySize = Math.floor(diff / 3) + 1;
    const category = Math.floor((nb - min) / categorySize);
    const color = this.tabColor[category];
    return color;
  }

  minNumber(list): number{
    let min = 185555555;
    for( const element in list ) {
      if( min > list[element].hospitalized ){
        min = list[element].hospitalized;
      }
    }
    return min;
  }

  maxNumber(list): number{
    let max = 0;
    for( const element in list ) {
      if( max < list[element].hospitalized ){
        max = list[element].hospitalized;
      }
    }
    return max;
  }

}
