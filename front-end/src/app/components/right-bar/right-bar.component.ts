import {Component, Input, OnInit, SimpleChanges, OnChanges} from '@angular/core';
import * as Highcharts from 'highcharts';
import {NgbCalendar, NgbDate, NgbPanelChangeEvent} from '@ng-bootstrap/ng-bootstrap';
import {MapService} from '../../service/map/map.service';
import {HistoriqueService} from '../../service/historique/historique.service';
import {SimulationService} from '../../service/simulation/simulation.service';

@Component({
  selector: 'app-right-bar',
  templateUrl: './right-bar.component.html',
  styleUrls: ['./right-bar.component.css']
})
export class RightBarComponent implements OnInit, OnChanges{
  @Input() locationName: string;
  @Input() isRegion: boolean;
  @Input() actualdate: NgbDate;
  @Input() SelectedMenu;
  @Input() isSimulationStarted: boolean;

  // Json of the region
  private reglist = [];
  // Json of the Dept
  private deptList;
  private allData = [];
  private casConf = [];
  private gueris = [];
  private hospi = [];
  private deces = [];
  private casConf2 = [];
  private gueris2 = [];
  private hospi2 = [];
  private deces2 = [];
  public totCasConf: string ;
  public totGueris ;
  public totHospi ;
  public totDeces ;
  // Json of France
  private histFr;

  public chosenLocation;


  public differences = [];

  public showLocation = false;
  public showLink = false;

  private dates = [];

  public options: any = {
    Chart: {
      type: 'area',
      height: 10,
      width: 10
    },
    title: {
      text: 'Evolution'
    },
    credits: {
      enabled: false
    },
    xAxis: {
      categories: this.dates ,
      tickmarkPlacement: 'on',
      title: {
        enabled: false
      }
    },
    yAxis: {
      tickInterval: 500,
      ceiling : 8000,
      floor : -5500
    },
    series: [{
      name: 'Cas confirmés',
      data: this.casConf
    }, {
      name: 'Décès',
      data: this.deces
    }, {
      name: 'Guéris',
      data: this.gueris
    }, {
      name: 'Hospitalisés',
      data: this.hospi
    }
    ]
  };

  public options2: any = {
    Chart: {
      type: 'area',
      height: 10,
      width: 10
    },
    title: {
      text: 'Evolution'
    },
    credits: {
      enabled: false
    },
    xAxis: {
      categories: this.dates ,
      tickmarkPlacement: 'on',
      title: {
        enabled: false
      }
    },
    yAxis: {
      tickInterval: 500,

    },
    series: [{
      name: 'Cas confirmés',
      data: this.casConf2
    }, {
      name: 'Décès',
      data: this.deces2
    }, {
      name: 'Guéris',
      data: this.gueris2
    }, {
      name: 'Hospitalisés',
      data: this.hospi2
    }
    ]
  };

  constructor(private simulationService: SimulationService, private mapService: MapService,
              private historiqueService: HistoriqueService, calendar: NgbCalendar)  {
    // Today's date
    this.actualdate = calendar.getToday();
    // set date to 2 days before today
    this.actualdate = calendar.getPrev(this.actualdate, 'd', 2);
  }

  ngOnInit() {
    this.getHFrance();
    this.showLocation = false;
    this.showLink = false;
    this.chosenLocation = 'France';
    this.locationName = 'France';
    // this.isSimulationStarted = false;
  }

  ngOnChanges(changes: SimpleChanges ){
    for (const propName in changes) {
      if (changes.hasOwnProperty(propName)) {
        switch (propName) {
          // When actualdate changes
          case 'actualdate':
          // When locationName changes
          case 'locationName': {
            if (this.locationName &&  this.locationName !== 'France' ) {// click sur region ou dpt
              if (this.isRegion) {
                this.getRegInfos();
                this.getHRegion();
              } else {
                this.getDeptInfos();
                this.getHDept();
              }
              this.showLocation = true;
              this.showLink = true;
            }else{                    // france
              this.showLink = false;
            }
            break;
          }
          // When isSimulationStarted changes
          case 'isSimulationStarted': {
            if (this.isSimulationStarted && this.locationName === 'France'){
              this.getHFrance();
              // this.showLocation = true;
            }
            break;
          }
          default:
            break;
        }
      }
    }
  }

  getRegInfos() {
    if (this.SelectedMenu === 'map'){
      this.mapService.getInfosRegion(this.actualdate, this.locationName).subscribe(
        data => {
          this.reglist = data;
          this.chosenLocation = data.name;
          this.totGueris = data.recoveredCases;
          this.totHospi = data.hospitalized;
          this.totDeces = data.totalDeaths;
          if (data.totalCases === 0){
            this.totCasConf = (parseInt(this.totGueris.toString(), 10) +
              parseInt(this.totHospi.toString(), 10) + parseInt(data.criticalCases.toString(), 10)).toString();
          }else{
            this.totCasConf = data.totalCases;
          }

        }
      );
    }else{
      this.simulationService.getInfosRegion(this.actualdate, this.locationName).subscribe(
        data => {
          this.reglist = data;
          this.chosenLocation = data.name;
          this.totGueris = data.recoveredCases;
          this.totHospi = data.hospitalized;
          this.totDeces = data.totalDeaths;
          if (data.totalCases === 0){
            this.totCasConf = (parseInt(this.totGueris.toString(), 10) +
              parseInt(this.totHospi.toString(), 10) + parseInt(data.criticalCases.toString(), 10)).toString();
          }else{
            this.totCasConf = data.totalCases;
          }

        }
      );


    }
  }

  getDeptInfos() {
    if (this.SelectedMenu === 'map'){
      this.mapService.getInfosDept(this.actualdate, this.locationName).subscribe(
        data => {
          this.deptList = data;
          this.chosenLocation = data.name;
          this.totGueris = data.recoveredCases;
          this.totHospi = data.hospitalized;
          this.totDeces = data.totalDeaths;
          if (data.totalCases === 0){
            this.totCasConf = (parseInt(this.totGueris.toString(), 10) +
              parseInt(this.totHospi.toString(), 10) + parseInt(data.criticalCases.toString(), 10)).toString();
          }else{
            this.totCasConf = data.totalCases;
          }
        }
      );
    }else{
      this.simulationService.getInfosDept(this.actualdate, this.locationName).subscribe(
        data => {
          this.deptList = data;
          this.chosenLocation = data.name;
          this.totGueris = data.recoveredCases;
          this.totHospi = data.hospitalized;
          this.totDeces = data.totalDeaths;
          if (data.totalCases === 0){
            this.totCasConf = (parseInt(this.totGueris.toString(), 10) +
              parseInt(this.totHospi.toString(), 10) + parseInt(data.criticalCases.toString(), 10)).toString();
          }else{
            this.totCasConf = data.totalCases;
          }
        }
      );
    }
  }

  getHFrance(){
    if (this.SelectedMenu === 'map'){
      this.historiqueService.getHistoriqueFrance().subscribe(
        data => {
          // this.histFr = data;
          this.setAllDataFromFrance(data);
        }
      );
    }else{
      this.simulationService.getInfosFrance(this.actualdate).subscribe(
        data => {
          // this.histFr = data;
          console.log('fra data :');
          console.log(data);
          this.totGueris = data.recoveredCases;
          this.totDeces = data.totalDeaths;
          this.totHospi = data.hospitalized;
          this.showLocation = true;
          if (data.totalCases === 0){
            this.totCasConf = (parseInt(this.totGueris.toString(), 10) +
              parseInt(this.totHospi.toString(), 10) + parseInt(data.criticalCases.toString(), 10)).toString();
          }else{
            this.totCasConf = data.totalCases;
          }
            // this.setAllDataFromFrance(data);
        }
      );

    }
  }

  getHRegion(){
    if (this.SelectedMenu === 'map'){
      this.historiqueService.getHistoriqueRegion(this.locationName).subscribe(
        data => {
          // this.histFr = data;
          // console.log(data);
          this.setAllDataFromFrance(data);
        }
      );
    }else{}
  }

  getHDept(){
    if (this.SelectedMenu === 'map'){
      this.historiqueService.getHistoriqueDept(this.locationName).subscribe(
        data => {
          // this.histFr = data;
          // console.log(data);
          this.setAllDataFromFrance(data);
        }
      );
    }else{}
  }

  // Get Data from list and put it in this.allData
  // Order this.allData by Date
  // set Graph
  setAllDataFromFrance(list) {
    this.allData.splice(0, this.allData.length);
    for (const index in list){
      const element = list[index];
      const elementData = {date: null , hospitalized: null, totalDeaths: null, recoveredCases: null, totalCases: null };
      const dateStruct = element.date;
      const currentDate = new Date(dateStruct.year, dateStruct.month - 1, dateStruct.day - 1);
      elementData.date = currentDate.toDateString();
      elementData.hospitalized = element.hospitalized;
      elementData.totalDeaths = element.totalDeaths;
      elementData.recoveredCases = element.recoveredCases;
      elementData.totalCases = element.totalCases;
      this.allData.push(elementData);
    }
    this.sortData(this.allData);
    this.setGraph(this.allData);
    Highcharts.chart('charts', this.options);
    Highcharts.chart('charts2', this.options2);
  }

  // Sort data by date
  sortData(list) {
    list.sort((a, b) => {
      return (new Date(a.date) as any) - (new Date(b.date) as any);
    });
  }

  // Set the table to construct the graph
  setGraph(list) {
    this.casConf.splice(0, this.casConf.length);
    this.hospi.splice(0, this.hospi.length);
    this.gueris.splice(0, this.gueris.length);
    this.deces.splice(0, this.deces.length);
    this.dates.splice(0, this.dates.length);
    for (const index in list) {
      if (parseInt(index, 10) !== 0 ){
        this.dates.push(list[index].date);
        this.casConf.push((list[index].totalCases) - (list[parseInt(index, 10) - 1].totalCases));
        this.hospi.push((list[index].hospitalized) - (list[parseInt(index, 10) - 1].hospitalized));
        this.deces.push((list[index].totalDeaths) - (list[parseInt(index, 10) - 1].totalDeaths));
        this.gueris.push((list[index].recoveredCases) - (list[parseInt(index, 10) - 1].recoveredCases));
      }
      this.casConf2.push(list[index].totalCases);
      this.hospi2.push(list[index].hospitalized);
      this.deces2.push(list[index].totalDeaths);
      this.gueris2.push(list[index].recoveredCases);

    }
  }




}
