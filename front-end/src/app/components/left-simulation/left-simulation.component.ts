import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {faCalendarAlt, faStop} from '@fortawesome/free-solid-svg-icons';
import { faPlay } from '@fortawesome/free-solid-svg-icons';
import { faPause } from '@fortawesome/free-solid-svg-icons';
import {NgbCalendar, NgbDate, NgbPanelChangeEvent} from '@ng-bootstrap/ng-bootstrap';
import {SimulationService} from '../../service/simulation/simulation.service';
import {DateServiceService} from '../../service/Date/date-service.service';
import {SimulParams} from '../../model/SimulParams';


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
  calendarIcon = faCalendarAlt;
  // Calendar
  startDateCalendarVisible: boolean;
  endDateCalendarVisible: boolean;
  // Params
  public allParams: SimulParams;
  // simulStatus
  public isStart: boolean;
  public isPause: boolean;
  public isStop: boolean;
  public simulationIsCompute: boolean;
  // Deals with time
  public simulDate: NgbDate;
  // Interval
  interval;
  public chosenInterval = 2;
  // display accordion
  displayAccordion: string;

  constructor(private simulationService: SimulationService, private calendar: NgbCalendar, public dateService: DateServiceService) {
  }

  ngOnInit(): void {
    this.simulationIsCompute = false;
    this.startDateCalendarVisible = false;
    this.endDateCalendarVisible = false;
    this.isStart = false;
    this.sendSimulStatus.emit(this.isStart);
    this.isPause = false;
    this.isStop = false;
    this.initializeParams();
  }

  initializeParams() {
    this.allParams = new SimulParams();
    this.allParams.startDate = new NgbDate(2020, 3, 18);
    this.allParams.endDate = new NgbDate(2020, 4, 30);
    this.simulDate = this.allParams.startDate;
    this.sendDate.emit(this.simulDate);
    this.displayAccordion = 'block';
  }

  startSimul(): boolean {
    console.log('sendParams');
    const isCompute = this.simulationService.startSimul(this.allParams);
    console.log(this.allParams);
    return isCompute;
  }

  onStart() {
    console.log('appui sur play');
    this.simulationIsCompute = false;
    this.isPause = false;
    this.isStart = true;
    this.isStop = false;
    this.displayAccordion = 'none';
    this.sendSimulStatus.emit(this.isStart);
    this.simulationIsCompute = this.startSimul();
    this.startTimer(this.simulDate, this.allParams.endDate);
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
    // Send simulation state
    this.sendSimulStatus.emit(this.isStart);
    // reset params
    this.initializeParams();
    // set reset param
    this.allParams.reset = true;
  }

  onChangeBorder() {
        this.allParams.borders = !this.allParams.borders;
        console.log('frontières fermées :' + this.allParams.borders);
    }

  onChangeShops() {
      this.allParams.shops = !this.allParams.shops;
      console.log('Commerces fermés :' + this.allParams.shops);
    }

  onChangeHosp() {
      this.allParams.hosp = !this.allParams.hosp;
      console.log('Répartition hospitalisés :' + this.allParams.hosp);
    }

  onChangeMask(category: string) {
    this.allParams.mask[category] = !this.allParams.mask[category];
    console.log('Confinement catégorie : ' + category + ' : ' + this.allParams.mask[category]);
  }

  onChangeConfinement(category: string) {
    this.allParams.conf[category] = !this.allParams.conf[category];
    console.log('Confinement catégorie : ' + category + ' : ' + this.allParams.conf[category]);
  }

  onStartDateSelect(date: NgbDate) {
    this.simulDate = date;
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
