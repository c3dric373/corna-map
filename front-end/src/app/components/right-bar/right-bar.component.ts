import {Component, Input, OnInit, SimpleChanges, OnChanges} from '@angular/core';
import * as Highcharts from 'highcharts';
import {NgbCalendar, NgbDate, NgbPanelChangeEvent} from '@ng-bootstrap/ng-bootstrap';
import {MapService} from '../../service/map/map.service';
import {HistoriqueService} from '../../service/historique/historique.service';
@Component({
  selector: 'app-right-bar',
  templateUrl: './right-bar.component.html',
  styleUrls: ['./right-bar.component.css']
})
export class RightBarComponent implements OnInit, OnChanges{
  @Input() locationName: string;
  @Input() isRegion: boolean;
  @Input() actualdate: NgbDate;

  // Json of the region
  private reglist = [];
  // Json of the Dept
  private deptList;
  private allData = [];
  private casConf = [];
  private gueris = [];
  private hospi = [];
  private deces = [];
  public totCasConf: string ;
  public totGueris ;
  public totHospi ;
  public totDeces ;
  // Json of France
  private histFr;

  private chosenLocation;

  public differences = [];

  public showLocation = false;

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

  constructor(private mapService: MapService, private historiqueService: HistoriqueService, calendar: NgbCalendar)  {
    // Today's date
    this.actualdate = calendar.getToday();
    // set date to 2 days before today
    this.actualdate = calendar.getPrev(this.actualdate, 'd', 2);
  }

  ngOnInit() {
    this.getHFrance();
    this.showLocation = false;
    this.chosenLocation = 'France';
  }

  ngOnChanges(composant: SimpleChanges ){
    if (this.locationName && this.locationName !== 'France') {
      if (this.isRegion) {
        this.getRegInfos();
        this.getHRegion();
      } else {
        this.getDeptInfos();
        this.getHDept();
      }
      this.showLocation = true;
    }
  }

  public beforeChange($event: NgbPanelChangeEvent) {
/*
    if (this.isRegion){
      this.getRegInfos();
    }else{
      this.getDeptInfos();
    }*/

  }

  getRegInfos() {
    this.mapService.getInfosRegion(this.actualdate, this.locationName).subscribe(
      data => {
        this.reglist = data;
        this.chosenLocation = data.name;
        console.log(data);

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

  getDeptInfos() {
    this.mapService.getInfosDept(this.actualdate, this.locationName).subscribe(
      data => {
        this.deptList = data;
        this.chosenLocation = data.name;
        console.log(data);

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

  getHFrance(){
    this.historiqueService.getHistoriqueFrance().subscribe(
      data => {
        // this.histFr = data;
        this.setAllDataFromFrance(data);
      }
    );
  }

  getHRegion(){
    this.historiqueService.getHistoriqueRegion(this.locationName).subscribe(
      data => {
        // this.histFr = data;
        // console.log(data);
        this.setAllDataFromFrance(data);
      }
    );
  }

  getHDept(){
    this.historiqueService.getHistoriqueDept(this.locationName).subscribe(
      data => {
        // this.histFr = data;
        // console.log(data);
        this.setAllDataFromFrance(data);
      }
    );
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
      // this.casConf.push(list[index].totalCases);
      // this.hospi.push(list[index].hospitalized);
      // this.deces.push(list[index].totalDeaths);
      // this.gueris.push(list[index].recoveredCases);

    }
  }




}
