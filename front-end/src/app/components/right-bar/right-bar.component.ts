import {Component, Input, OnInit, SimpleChanges, OnChanges, Output, EventEmitter} from '@angular/core';
import * as Highcharts from 'highcharts';
import {NgbCalendar, NgbDate, NgbPanelChangeEvent} from '@ng-bootstrap/ng-bootstrap';
import {MapService} from '../../service/map/map.service';
import {HistoriqueService} from '../../service/historique/historique.service';
import {SimulationService} from '../../service/simulation/simulation.service';
import {faCompressAlt, faExpandAlt} from '@fortawesome/free-solid-svg-icons';

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
  @Input() isPaused: boolean;

  @Output() isOnlyGraph = new EventEmitter<boolean>();

  // Icons
  expand = faExpandAlt;
  compress = faCompressAlt;
  // Json of the region
  private reglist = [];
  // Json of the Dept
  private deptList;
  // Tables
  private allData = [];
  private casConf = [];
  private gueris = [];
  private hospi = [];
  private deces = [];
  private critiques = [];
  private casConf2 = [];
  private gueris2 = [];
  private hospi2 = [];
  private deces2 = [];
  private critiques2 = [];
  public totCasConf: string ;
  public totGueris ;
  public totHospi ;
  public totDeces ;
  public totCritiques;
  // Extand status
  public onlyGraph: boolean;
  // Chosen location
  public chosenLocation;
  public showLocation = false;
  // show 'Revenir à la France'
  public showLink = false;
  // Date table
  private dates = [];

  public options: any = {
    Chart: {
      type: 'area',
      height: 50,
      width: 50,
    },
    title: {
      text: 'Variation journalière'
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
      tickInterval: 1,
      /*ceiling : 8000,
      floor : -5500*/
    },
    series: [{
      name: 'Cas confirmés',
      data: this.casConf ,
      color : '#ffd966'
    }, {
      name: 'Décès',
      data: this.deces ,
      color : '#171616'
    }, {
      name: 'Guéris',
      data: this.gueris ,
      color : '#6ca644ff'
    }, {
      name: 'Hospitalisés',
      data: this.hospi ,
      color : '#fd9d3dff'
    }
    , {
        name: 'Cas critiques',
        data: this.critiques ,
        color : '#a50026ff'
      }
      ]
  };

  public options2: any = {
    Chart: {
      type: 'area',
      height: 10,
      width: 10,
    },
    title: {
      text: 'Evolution globale'
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
      data: this.casConf2 ,
      color : '#ffd966'
    }, {
      name: 'Décès',
      data: this.deces2 ,
      color : '#171616'
    }, {
      name: 'Guéris',
      data: this.gueris2 ,
      color : '#6ca644ff'
    }, {
      name: 'Hospitalisés',
      data: this.hospi2 ,
      color : '#fd9d3dff'
    }
      , {
        name: 'Cas critiques',
        data: this.critiques2 ,
        color : '#a50026ff'
      }
    ]
  };

  constructor(private simulationService: SimulationService, private mapService: MapService,
              private historiqueService: HistoriqueService, calendar: NgbCalendar)  {
    // Today's date
    this.actualdate = calendar.getToday();
    // set date to 2 days before today
    this.actualdate = calendar.getPrev(this.actualdate, 'd', 2);
    // set extand
    this.onlyGraph = false;
  }

  ngOnInit() {
    this.isPaused = false;
    this.resetGraph();
    this.getHFrance();
    this.showLocation = false;
    this.showLink = false;
    this.chosenLocation = 'France';
    this.locationName = 'France';
  }

  ngOnChanges(changes: SimpleChanges ){
    for (const propName in changes) {
      if (changes.hasOwnProperty(propName)) {
        switch (propName) {
          // When isPaused changes
          case 'isPaused':
          // When actualdate changes
          case 'actualdate': {
            this.inputChange();
            break;
          }
          // When isSimulationStarted changes
          case 'isSimulationStarted':
          // When locationName changes
          case 'locationName': {
            this.resetGraph();
            this.inputChange();
            break;
          }
          default:
            break;
        }
      }
    }
  }

  inputChange(){
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
    if (this.isSimulationStarted && this.locationName === 'France'){
      this.getHFrance();
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
          this.totCritiques = data.criticalCases;
          if (data.totalCases === 0){
            this.totCasConf = (parseInt(this.totGueris.toString(), 10) +
              parseInt(this.totHospi.toString(), 10) /*+ parseInt(this.totGueris.toString(), 10)*/
              + parseInt(data.criticalCases.toString(), 10)).toString();
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
          this.totHospi = '-'; // data.hospitalized;
          this.totDeces = data.totalDeaths;
          this.totCritiques = '-'; // data.criticalCases;
          if (data.totalCases === 0){
            this.totCasConf = (parseInt(this.totGueris.toString(), 10) +
              parseInt(this.totHospi.toString(), 10) /* + parseInt(this.totGueris.toString(), 10) */
              + parseInt(data.criticalCases.toString(), 10)).toString();
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
          this.totCritiques = data.criticalCases;
          if (data.totalCases === 0){
            this.totCasConf = (parseInt(this.totGueris.toString(), 10) +
              parseInt(this.totHospi.toString(), 10) /* + parseInt(this.totGueris.toString(), 10) */ +
              parseInt(data.criticalCases.toString(), 10)).toString();
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
          this.totHospi = '-'; // data.hospitalized;
          this.totDeces = data.totalDeaths;
          this.totCritiques = '-'; // data.criticalCases;
          if (data.totalCases === 0){
            this.totCasConf = (parseInt(this.totGueris.toString(), 10) +
              parseInt(this.totHospi.toString(), 10) /* + parseInt(this.totGueris.toString(), 10) */
              + parseInt(data.criticalCases.toString(), 10)).toString();
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
          this.setAllDataFromFrance(data);
        }
      );
    }else{
      this.simulationService.getInfosFrance(this.actualdate).subscribe(
        data => {
          this.totGueris = data.recoveredCases;
          this.totDeces = data.totalDeaths;
          this.totHospi = '-'; // data.hospitalized;
          this.totCritiques = '-'; // data.criticalCases;
          this.showLocation = true;
          if (data.totalCases === 0){
            this.totCasConf = (parseInt(this.totGueris.toString(), 10) +
              parseInt(this.totHospi.toString(), 10) /* + parseInt(this.totGueris.toString(), 10) */
              + parseInt(data.criticalCases.toString(), 10)).toString();
          }else{
            this.totCasConf = data.totalCases;
          }
          this.setGraphSimulation(data);
          Highcharts.chart('charts', this.options);
          Highcharts.chart('charts2', this.options2);

        }
      );

    }
  }

  getHRegion(){
    if (this.SelectedMenu === 'map'){
      this.historiqueService.getHistoriqueRegion(this.locationName).subscribe(
        data => {
          this.setAllDataFromFrance(data);
        }
      );
    }else{
      this.simulationService.getInfosRegion( this.actualdate , this.locationName).subscribe(
        data => {
          this.totGueris = data.recoveredCases;
          this.totDeces = data.totalDeaths;
          this.totHospi = '-'; // data.hospitalized;
          this.totCritiques = '-'; // data.criticalCases;
          this.showLocation = true;
          if (data.totalCases === 0){
            this.totCasConf = (parseInt(this.totGueris.toString(), 10) +
              parseInt(this.totHospi.toString(), 10) /* + parseInt(this.totGueris.toString(), 10) */
              + parseInt(data.criticalCases.toString(), 10)).toString();
          }else{
            this.totCasConf = data.totalCases;
          }
          this.setGraphSimulation(data);
          Highcharts.chart('charts', this.options);
          Highcharts.chart('charts2', this.options2);

        }
      );
    }
  }

  getHDept(){
    if (this.SelectedMenu === 'map'){
      this.historiqueService.getHistoriqueDept(this.locationName).subscribe(
        data => {
          this.setAllDataFromFrance(data);
        }
      );
    }else{
      this.simulationService.getInfosDept( this.actualdate , this.locationName).subscribe(
        data => {
          this.totGueris = data.recoveredCases;
          this.totDeces = data.totalDeaths;
          this.totHospi = '-'; // data.hospitalized;
          this.totCritiques = '-'; // data.criticalCases;
          this.showLocation = true;
          if (data.totalCases === 0){
            this.totCasConf = (parseInt(this.totGueris.toString(), 10) +
              parseInt(this.totHospi.toString(), 10)
              + parseInt(data.criticalCases.toString(), 10)).toString();
          }else{
            this.totCasConf = data.totalCases;
          }
          this.setGraphSimulation(data);
          Highcharts.chart('charts', this.options);
          Highcharts.chart('charts2', this.options2);

        }
      );
    }
  }

  // Get Data from list and put it in this.allData
  // Order this.allData by Date
  // set Graph
  setAllDataFromFrance(list) {
    this.allData.splice(0, this.allData.length);
    for (const index in list){
      const element = list[index];
      const elementData = {date: null , hospitalized: null, totalDeaths: null, recoveredCases: null, totalCases: null, criticalCases : null };
      const dateStruct = element.date;
      const currentDate = new Date(dateStruct.year, dateStruct.month - 1, dateStruct.day - 1);
      elementData.date = currentDate.toDateString();
      elementData.hospitalized = element.hospitalized;
      elementData.totalDeaths = element.totalDeaths;
      elementData.recoveredCases = element.recoveredCases;
      elementData.totalCases = element.totalCases;
      elementData.criticalCases = element.criticalCases;
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

  resetGraph(){
    this.casConf.splice(0, this.casConf.length);
    this.hospi.splice(0, this.hospi.length);
    this.gueris.splice(0, this.gueris.length);
    this.deces.splice(0, this.deces.length);
    this.dates.splice(0, this.dates.length);
    this.critiques.splice(0, this.critiques.length);
    this.casConf2.splice(0, this.casConf2.length);
    this.hospi2.splice(0, this.hospi2.length);
    this.gueris2.splice(0, this.gueris2.length);
    this.deces2.splice(0, this.deces2.length);
    this.critiques2.splice(0, this.critiques2.length);
  }

  // Set the table to construct the graph
  setGraph(list) {
    this.resetGraph();
    for (const index in list) {
      this.dates.push(list[index].date);
      this.casConf2.push(list[index].totalCases);
      this.hospi2.push(list[index].hospitalized);
      this.deces2.push(list[index].totalDeaths);
      this.gueris2.push(list[index].recoveredCases);
      this.critiques2.push(list[index].criticalCases);
      if (this.casConf2.length > 1){
        this.casConf.push(this.casConf2[this.casConf2.length - 1  ] - this.casConf2[this.casConf2.length - 2 ]);
        this.hospi.push(this.hospi2[this.hospi2.length - 1 ] - this.hospi2[this.hospi2.length - 2 ]);
        this.deces.push(this.deces2[this.deces2.length - 1 ] - this.deces2[this.deces2.length - 2 ]);
        this.gueris.push(this.gueris2[this.gueris2.length - 1 ] - this.gueris2[this.gueris2.length - 2 ]);
        this.critiques.push(this.critiques2[this.critiques2.length - 1 ] - this.critiques2[this.critiques2.length - 2 ]);
      }
    }
  }

  setGraphSimulation(data){
    if (!this.isSimulationStarted){
      this.resetGraph();
    }else{
      const dateStruct = data.date;
      const currentDate = new Date(dateStruct.year, dateStruct.month - 1, dateStruct.day - 1);
      this.dates.push(currentDate.toDateString());
    }
    this.casConf2.push(data.totalCases);
    // this.hospi2.push(data.hospitalized);
    this.deces2.push(data.totalDeaths);
    this.gueris2.push(data.recoveredCases);
    // this.critiques2.push(data.criticalCases);
    if (this.casConf2.length > 1){
      this.casConf.push(this.casConf2[this.casConf2.length - 1 ] - this.casConf2[this.casConf2.length - 2 ]);
      // this.hospi.push(this.hospi2[this.hospi2.length - 1 ] - this.hospi2[this.hospi2.length - 2 ]);
      this.deces.push(this.deces2[this.deces2.length - 1 ] - this.deces2[this.deces2.length - 2 ]);
      this.gueris.push(this.gueris2[this.gueris2.length - 1 ] - this.gueris2[this.gueris2.length - 2 ]);
      // this.critiques.push(this.critiques2[this.critiques2.length - 1 ] - this.critiques2[this.critiques2.length - 2 ]);
    }
  }

  onClickExtend(): void {
    const chart1 = Highcharts.chart('charts', this.options);
    const chart2 = Highcharts.chart('charts2', this.options2);
    this.onlyGraph = !this.onlyGraph;
    this.isOnlyGraph.emit(this.onlyGraph);
    setTimeout(() => {
      chart1.reflow();
      chart2.reflow(); } , 5);
  }

}
