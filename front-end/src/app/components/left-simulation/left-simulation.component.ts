import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
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
  @Output() sendDate = new EventEmitter<NgbDate>();
  @Output() sendSimulStatus = new EventEmitter<boolean>();

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
  // display accordion
  displayAccordion: string;

  constructor(private simulationService: SimulationService, private calendar: NgbCalendar, public dateService: DateServiceService) {
    this.endDate = new NgbDate(2020, 5, 31);
  }

  ngOnInit(): void {
    this.isStart = false;
    this.sendSimulStatus.emit(this.isStart);
    this.isPause = false;
    this.isStop = false;
    this.initializeParams();
  }

  initializeParams() {
    // Params to send
    this.borders = false;
    this.shops = false;
    this.hosp = false;
    this.respectConfinement = 50;
    this.mask = [false, false, false, false, false];
    this.conf = [false, false, false, false, false];
    this.resetSim = false;

    this.simulDate = new NgbDate(2020, 3, 18);
    this.sendDate.emit(this.simulDate);

    this.displayAccordion = 'block';
  }

  sendParams(){
    console.log('sendParams');
    this.simulationService.sendParams([this.resetSim, this.dateService.dateToString(this.simulDate),
      this.conf, this.borders, this.shops, this.hosp , this.mask, this.respectConfinement]);
    return [this.resetSim, this.dateService.dateToString(this.simulDate),
      this.conf, this.borders, this.shops, this.hosp , this.mask, this.respectConfinement];
  }

  onStart() {
    console.log('appui sur play');
    this.isPause = false;
    this.isStart = true;
    this.isStop = false;
    this.displayAccordion = 'none';
    this.sendSimulStatus.emit(this.isStart);
    console.log(this.sendParams());
    this.startTimer(this.simulDate, this.endDate);
  }

  onPause() {
    console.log('appui sur pause');
    this.isPause = true;
    this.isStart = false;
    this.isStop = false;
    this.displayAccordion = 'block';
    this.sendSimulStatus.emit(this.isStart);
  }

  onstop() {
    console.log('appui sur stop');
    this.isPause = true;
    this.isStart = false;
    this.isStop = true;
    this.displayAccordion = 'block';
    this.sendSimulStatus.emit(this.isStart);
    this.resetSim = true;
    this.initializeParams();
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
    console.log('Masque catégorie : m' + int + ' : ' + this.mask[int]);
  }

  onChangeConfinement(int) {
    this.conf[int] = !this.conf[int];
    console.log('Confinement catégorie : c' + int + ' : ' + this.conf[int]);
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

  startTimer(startDate: NgbDate, endDate: NgbDate) {
    let currentDate = startDate;
    this.interval = setInterval(() => {
      if (!this.isPause) {
        if (currentDate.before(endDate)) {
          this.simulDate = currentDate;
          // Send Date to component
          this.sendDate.emit(this.simulDate);
          currentDate = this.calendar.getNext(currentDate, 'd', 1);
        } else {
          clearInterval(this.interval);
          this.onstop();
        }
      } else {
        clearInterval(this.interval);
      }
    }, (this.chosenInterval * 1000));
  }

}
