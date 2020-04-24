import { Component, OnInit } from '@angular/core';
import { faStop } from '@fortawesome/free-solid-svg-icons';
import { faPlay } from '@fortawesome/free-solid-svg-icons';
import { faPause } from '@fortawesome/free-solid-svg-icons';
import { NgbPanelChangeEvent } from '@ng-bootstrap/ng-bootstrap';



@Component({
  selector: 'app-left-simulation',
  templateUrl: './left-simulation.component.html',
  styleUrls: ['./left-simulation.component.css']
})
export class LeftSimulationComponent implements OnInit {


  constructor() { }
  faStop = faStop;
  faPlay = faPlay;
  faPause = faPause;
  public play;
  public pause;
  public stop;
  public selectedConfinement: string;
  public tabConfinement = ['Aucun', 'Pour tous', '+60 ans' ];
  public borders;
  public shops;
  public hosp;
  public mask = [false, false, false, false, false];
  public conf = [false, false, false, false, false];
  public respectConfinement;
  public timer;

    ngOnInit(): void {
      this.selectedConfinement = this.tabConfinement[0];
      this.borders = false;
      this.shops = false;
      this.hosp = false;
      this.respectConfinement = 50;
      this.timer = 0;
      this.play = false;
      this.pause = false;
      this.stop = true;
  }



onChangeCategory(category) {
    this.selectedConfinement = category;
    console.log('confinement : ' + this.selectedConfinement);
  }

onChangeBorder() {
      this.borders = !this.borders;
      console.log('frontières fermées :' + this.borders);
  }

onChangeShops() {
    this.shops = !this.shops;
    console.log('Commerces fermés :' + this.shops);
  }

onChangeHosp() {
    this.hosp = !this.hosp;
    console.log('Répartition hospitalisés :' + this.hosp);
  }

onChangeMask(int) {
    this.mask[int] = !this.mask[int];
    if (this.mask[int] === true){

      document.getElementById(int).style.backgroundColor = '#B1AEAA';
    }else{
      document.getElementById(int).style.backgroundColor = '#CFCDCC';
    }
    console.log('Masque catégorie :' + int + ' : ' + this.mask[int]);
  }

  onChangeConfinement(int) {
    this.conf[int] = !this.conf[int];
    if (this.conf[int] === true){
      document.getElementById(int).style.backgroundColor = '#B1AEAA';
    }else{
      document.getElementById(int).style.backgroundColor = '#CFCDCC';
    }
    console.log('Confinement catégorie :' + int + ' : ' + this.conf[int]);
  }


onChangeRespectConfinement(value: number) {
    if (value >= 1000) {
      return Math.round(value / 1000) + 'k';
    }
    this.respectConfinement = value;
    console.log('respectConfinement' + this.respectConfinement + '%');
    return (value + '%');
  }

  public beforeChange($event: NgbPanelChangeEvent) {


  }

}
