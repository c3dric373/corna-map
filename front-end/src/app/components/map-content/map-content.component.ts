import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MapService} from '../../service/map/map.service';
import {NgbCalendar, NgbDate, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import { faCalendarAlt, faExpandAlt, faCompressAlt} from '@fortawesome/free-solid-svg-icons';
import {SimulationService} from '../../service/simulation/simulation.service';

@Component({
  selector: 'app-map-content',
  templateUrl: './map-content.component.html',
  styleUrls: ['./map-content.component.css']
})
export class MapContentComponent implements OnInit {
  // Icon
  calendarIcon = faCalendarAlt;
  expand = faExpandAlt;
  compress = faCompressAlt;

  isRegion: boolean;
  onlyMap: boolean;
  // Color for gradient
  private rgbYellow = [244, 165, 130];
  private rgbRed = [178, 24, 43];
  // list of function used in initializeMapReg and initializeMapDept
  private mousOverReg;
  private mousLeaveReg;
  private mousOverDept;
  private mousLeaveDept;
  // Json of the region
  private reglist;
  // Json of the Dept
  private deptList;
  // Selected category : ex : nbDeath, ...
  public selectedCategory: string;
  public tabCategory = ['cas hospitalisés', 'cas critiques', 'nombre de morts', 'cas soignés' ];
  // date elements
  public date: NgbDate;
  model: NgbDateStruct;

  // Input
  @Input() SelectedMenu;

  //  Output attributes used in map-menu
  @Output() chosenLocation = new EventEmitter<string>();
  @Output() boolIsRegion = new EventEmitter<boolean>();
  @Output() loading = new EventEmitter<boolean>();
  @Output() isOnlyMap = new EventEmitter<boolean>();
  @Output() emitDate = new EventEmitter<NgbDate>();

  constructor(private mapService: MapService, private simulation: SimulationService, calendar: NgbCalendar) {
    this.date = calendar.getPrev(calendar.getToday());
    this.model = calendar.getPrev(calendar.getToday());
    this.mousOverReg = new Object();
    this.mousOverDept = new Object();
    this.mousLeaveDept = new Object();
    this.mousLeaveReg = new Object();
    this.onlyMap = false;
  }

  ngOnInit(): void {
    this.isOnlyMap.emit(false);
    this.loading.emit(true);
    this.isRegion = true;
    this.boolIsRegion.emit(true);
    this.getRegInfos();
    this.selectedCategory = this.tabCategory[0];
    this.emitDate.emit(this.date);
  }

  dispReg(): void {
    if(!this.isRegion) {
      this.isRegion = true;
      this.boolIsRegion.emit(true);
      this.removeDeptListener();
      this.initializeMapReg();
    }
  }

  dispDept(): void {
    if(this.isRegion) {
      this.isRegion = false;
      this.boolIsRegion.emit(false);
      this.removeRegListener();
      // set deptList if it has not been initialized
      if(!this.deptList) {
        this.getDeptInfos();
      } else {
        this.initializeMapDept();
      }
    }
  }

  getRegInfos() {
    if (this.SelectedMenu === 'map') {
      this.mapService.getMapRegion(this.date).subscribe(
        data => {
          this.reglist = data;
          this.initializeMapReg();
        }
      );
    } else {
      this.simulation.getMapRegion(this.date).subscribe(
        data => {
          this.reglist = data;
          this.initializeMapReg();
        }
      );
    }
  }

  getDeptInfos() {
    if (this.SelectedMenu === 'map') {
      this.mapService.getMapDept(this.date).subscribe(
        data => {
          this.deptList = data;
          this.initializeMapDept();
        }
      );
    } else {
      this.simulation.getMapDept(this.date).subscribe(
        data => {
          this.deptList = data;
          this.initializeMapDept();
        }
      );
    }
  }

  initializeMapDept() {
    for (const index in this.deptList) {
      const depName = this.deptList[index].nom;
      const nbHospitalized = this.getNbCas(index,  this.deptList);
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
      const nbHospitalized = this.getNbCas(index,  this.reglist);
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
    this.loading.emit(false);
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

  // Assign a gradient of color depending on the number provided in param
  assignColor(nb, list): string{
    const min = this.minNumber(list);
    const max = this.maxNumber(list);
    const coeff = (nb - min) / (max - min);
    const colorTab = [];
    for (let i = 0; i < this.rgbRed.length; i++ ) {
      const value = this.rgbYellow[i] * (1 - coeff) + this.rgbRed[i] * coeff;
      colorTab.push(value);
    }
    const color = 'rgb(' + colorTab[0] + ', ' +  colorTab[1] + ', ' + colorTab[2] + ')';
    return color;
  }

  // get the min number of the list
  minNumber(list): number{
    let min = 185555555;
    for ( const element in list ) {
      const nbCas = this.getNbCas(element, list);
      if ( min > nbCas ){
        min = nbCas;
      }
    }
    return min;
  }

  // Get the max number of the list
  maxNumber(list): number{
    let max = 0;
    for ( const element in list ) {
      const nbCas = this.getNbCas(element, list);
      if ( max < nbCas ){
        max = nbCas;
      }
    }
    return max;
  }

  // Get the number of case depending of the selected category
  // ex : if selectedCategory = 'cas hospitalisé',
  //      then we need to get the number of hospitalized from the list
  getNbCas(index, list): number{
    let nbCas;
    switch (this.selectedCategory){
      case 'cas hospitalisés' :
        nbCas = list[index].hospitalized;
        break;
      case 'cas critiques' :
        nbCas = list[index].criticalCases;
        break;
      case 'nombre de morts' :
        nbCas = list[index].totalDeaths;
        break;
      case 'cas soignés' :
        nbCas = list[index].recoveredCases;
        break;
    }
    return nbCas;
  }

  clickReg(region): void{
    if (this.isRegion) {
      this.chosenLocation.emit(region);
    }
  }

  clickDept(departement): void{
    if ( !this.isRegion ) {
      this.chosenLocation.emit(departement);
    }
  }

  onChangeCategory(category): void{
    this.selectedCategory = category;
    if (this.isRegion) {
      this.removeRegListener();
      this.initializeMapReg();
    } else {
      this.removeDeptListener();
      this.initializeMapDept();
    }
  }

  onDateSelect(date){
    this.date = date;
    this.emitDate.emit(this.date);
    // Color map
    if (this.isRegion) {
      this.removeRegListener();
      this.getRegInfos();
    } else {
      this.removeDeptListener();
      this.getDeptInfos();
    }
  }

  onClickExtend(): void {
    this.onlyMap = ! this.onlyMap;
    this.isOnlyMap.emit(this.onlyMap);
  }

}
