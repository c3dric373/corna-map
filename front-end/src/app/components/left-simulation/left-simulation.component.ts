import {Component, Input, OnInit} from '@angular/core';
import { faStop } from '@fortawesome/free-solid-svg-icons';
import { faPlay } from '@fortawesome/free-solid-svg-icons';
import { faPause } from '@fortawesome/free-solid-svg-icons';
import {NgbCalendar, NgbDate, NgbPanelChangeEvent} from '@ng-bootstrap/ng-bootstrap';
import {SimulationService} from '../../service/simulation/simulation.service';
import {MapService} from '../../service/map/map.service';
import {DateServiceService} from '../../service/Date/date-service.service';


@Component({
  selector: 'app-left-simulation',
  templateUrl: './left-simulation.component.html',
  styleUrls: ['./left-simulation.component.css']
})
export class LeftSimulationComponent implements OnInit {
  @Input() locationName: string;
  @Input() isRegion: boolean;

  // Icons
  faStop = faStop;
  faPlay = faPlay;
  faPause = faPause;
  // Params
  public borders;
  public shops;
  public hosp;
  public mask ;
  public conf ;
  public respectConfinement;
  public timer;
  public resetSim;
  // simulStatus
  public isStart: boolean;
  public isPause: boolean;
  public isStop: boolean;
  // Deals with time
  public simulDate: NgbDate;
  public endDate: NgbDate;
  // Interval
  interval;
  public chosenInterval = 2;

  constructor(private simulationService: SimulationService, private calendar: NgbCalendar, public dateService: DateServiceService) {
    this.simulDate = calendar.getToday();
    this.endDate = new NgbDate(2020, 5, 31);
  }

  ngOnInit(): void {
    this.isStart = false;
    this.isPause = false;
    this.isStop = false;
    this.initializeParams();
    this.onChangeTime(0);
  }

  initializeParams() {
    // Params to send
    this.borders = false;
    this.shops = false;
    this.hosp = false;
    this.respectConfinement = 50;
    this.mask = [false, false, false, false, false];
    this.conf = [false, false, false, false, false];
    this.timer = 0;
    this.resetSim = false;

    this.simulDate = this.calendar.getToday();
  }

  sendParams(){
    console.log('sendParams');
    this.simulationService.sendParams([this.resetSim, this.locationName,
      this.dateService.dateToString(this.simulDate), this.conf, this.borders,
      this.shops, this.hosp , this.mask, this.respectConfinement]);
    return [this.resetSim, this.locationName, this.dateService.dateToString(this.simulDate),
      this.conf, this.borders, this.shops, this.hosp , this.mask,
      this.respectConfinement];
  }

  onStart() {
    console.log('appui sur play');
    document.getElementById('accordion').style.display = 'none';
    this.isPause = false;
    this.isStart = true;
    this.isStop = false;
    console.log(this.sendParams());
    this.startTimer(this.simulDate, this.endDate);
  }

  onPause() {
    console.log('appui sur pause');
    this.isPause = true;
    this.isStart = false;
    this.isStop = false;
    document.getElementById('accordion').style.display = 'block';
  }

  onstop() {
    console.log('appui sur stop');
    this.isPause = true;
    this.isStart = false;
    this.isStop = true;
    document.getElementById('accordion').style.display = 'block';
    this.resetSim = true;
    this.initializeParams();
  }

  getInformations() {
    if (this.locationName === undefined) {
      console.log('appel getInfosFrance');
      this.simulationService.getInfosFrance(this.simulDate).subscribe(
        data => {
          console.log(data);
          // this.deptList = data;
          // this.initializeMapDept();
        }
      );
    } else if (!this.isRegion){
      console.log('appel getInfosDept');
      this.simulationService.getInfosDept(this.simulDate, this.locationName).subscribe(
        data => {
          console.log(data);
          // this.deptList = data;
          // this.initializeMapDept();
        }
      );
    }else{
      console.log('appel getInfosRegion');
      this.simulationService.getInfosRegion(this.simulDate, this.locationName).subscribe(
        data => {
          console.log(data);
          // this.deptList = data;
          // this.initializeMapDept();
        }
      );
    }
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

  onChangeEcoulement(value: number) {
    if (value >= 1000) {
      return Math.round(value / 1000) + 'k';
    }
    this.chosenInterval = value;
    console.log('temps change tout le' + this.chosenInterval + 'secondes');
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
      if (currentDate.before(endDate) && !this.isPause) {
        this.getInformations();
        this.simulDate = currentDate;
        currentDate = this.calendar.getNext(currentDate, 'd', 1);
      } else if (this.isStop) {
        clearInterval(this.interval);
        this.onstop();
      } else {
        clearInterval(this.interval);
      }
    }, (this.chosenInterval * 1000));
  }

}
