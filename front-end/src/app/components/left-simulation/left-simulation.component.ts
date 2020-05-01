import {Component, Input, OnInit} from '@angular/core';
import { faStop } from '@fortawesome/free-solid-svg-icons';
import { faPlay } from '@fortawesome/free-solid-svg-icons';
import { faPause } from '@fortawesome/free-solid-svg-icons';
import {NgbCalendar, NgbDate, NgbPanelChangeEvent} from '@ng-bootstrap/ng-bootstrap';
import {SimulationService} from '../../service/simulation/simulation.service';
import {MapService} from '../../service/map/map.service';


@Component({
  selector: 'app-left-simulation',
  templateUrl: './left-simulation.component.html',
  styleUrls: ['./left-simulation.component.css']
})
export class LeftSimulationComponent implements OnInit {
  @Input() locationName: string;
  @Input() isRegion: boolean;
  @Input() actualdate: NgbDate;

  // essai
  public essaiDate: NgbDate;
  public endDate: NgbDate;
  public chosenInterval = 5;
  interval;

  constructor(private simulationService: SimulationService, private calendar: NgbCalendar) {
    this.actualdate = calendar.getToday();
    this.essaiDate = calendar.getToday();
    this.endDate = new NgbDate(2020, 5, 31);
  }

  faStop = faStop;
  faPlay = faPlay;
  faPause = faPause;

  // public selectedConfinement: string;
  // public tabConfinement = ['Aucun', 'Pour tous', '+60 ans' ];
  public borders;
  public shops;
  public hosp;
  public mask ;
  public conf ;
  public respectConfinement;
  public timer;
  public resetSim;
  public pause = false;

    ngOnInit(): void {
      // this.selectedConfinement = this.tabConfinement[0];
      this.borders = false;
      this.shops = false;
      this.hosp = false;
      this.respectConfinement = 50;
      this.mask = [false, false, false, false, false];
      this.conf = [false, false, false, false, false];
      this.timer = 0;
      this.resetSim = false;
      this.pause = true;
      this.onChangeTime(0);
  }

  sendParams(){
      console.log('sendParams');

      this.simulationService.sendParams([this.resetSim, this.locationName,
        this.simulationService.dateToString(this.actualdate), this.conf, this.borders,
        this.shops, this.hosp , this.mask, this.respectConfinement]);

      return [this.resetSim, this.locationName, this.simulationService.dateToString(this.actualdate),
        this.conf, this.borders, this.shops, this.hosp , this.mask,
        this.respectConfinement];
  }

  onChangeSim(int) {
      switch (int) {
        case 0 :
          console.log('appui sur pause');
          this.pause = true;
          this.startTimer(this.essaiDate, this.endDate);
          document.getElementById('pause').setAttribute('disabled', 'disabled');
          document.getElementById('play').removeAttribute('disabled');
          document.getElementById('stop').removeAttribute('disabled');
          document.getElementById('accordion').style.display = 'block';
          break;
        case 1 :
          console.log('appui sur play');
          this.pause = false;
          document.getElementById('accordion').style.display = 'none';
          document.getElementById('play').setAttribute('disabled', 'disabled');
          document.getElementById('pause').removeAttribute('disabled');
          document.getElementById('stop').removeAttribute('disabled');
          console.log(this.sendParams());

          // essai
          this.startTimer(this.essaiDate, this.endDate);

          if (this.locationName === undefined) {
            console.log('appel getInfosFrance');
            this.simulationService.getInfosFrance(this.actualdate).subscribe(
              data => {
                console.log(data);
                 // this.deptList = data;
                 // this.initializeMapDept();
              }
            );
          } else if (!this.isRegion){
            console.log('appel getInfosDept');
            this.simulationService.getInfosDept(this.actualdate, this.locationName).subscribe(
              data => {
                console.log(data);
                // this.deptList = data;
                // this.initializeMapDept();
              }
            );
          }else{
            console.log('appel getInfosRegion');
            this.simulationService.getInfosRegion(this.actualdate, this.locationName).subscribe(
              data => {
                console.log(data);
                // this.deptList = data;
                // this.initializeMapDept();
              }
            );
          }
          break;
        case 2 :
          console.log('appui sur stop');
          this.pause = false;
          this.resetSim = true;
          document.getElementById('stop').setAttribute('disabled', 'disabled');
          document.getElementById('play').removeAttribute('disabled');
          document.getElementById('pause').removeAttribute('disabled');
          document.getElementById('accordion').style.display = 'block';
          console.log(this.sendParams());
          this.essaiDate = this.actualdate;
          this.ngOnInit();
          break;
        default:
          break;
      }

  }

/*onChangeCategory(category) {
    this.selectedConfinement = category;
    console.log('confinement : ' + this.selectedConfinement);
  }*/

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

  onChangeEcoulement(value: number) {
    if (value >= 1000) {
      return Math.round(value / 1000) + 'k';
    }
    this.chosenInterval = value;
    console.log('temps change tout le' + value + 'secondes');
    return (value + 's');
  }

  public beforeChange($event: NgbPanelChangeEvent) {


  }

  onChangeTime(value: number) {
      while (true){
        if (value >= 1000) {
          return Math.round(value / 1000) + 'k';
        }
        value++;
        console.log('time' + value );
        return (value );
      }

  }

  startTimer(startDate: NgbDate, endDate: NgbDate) {
    let currentDate = startDate;
    this.interval = setInterval(() => {
      if (currentDate.before(endDate) && !this.resetSim && !this.pause) {
        currentDate = this.calendar.getNext(currentDate, 'd', 1);
        this.essaiDate = currentDate;
      } else {
        clearInterval(this.interval);
      }
    }, (this.chosenInterval * 1000));
  }

}
