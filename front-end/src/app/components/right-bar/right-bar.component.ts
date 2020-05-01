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
  private reglist;
  // Json of the Dept
  private deptList;
  private allData = [];
  private casConf = [];
  private gueris = [];
  private hospi = [];
  private deces = [];
  // Json of France
  private histFr;

  private dates = [];

  public options: any = {
    Chart: {
      type: 'area',
      height: 100,
      width: 100
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
    series: [{
      name: 'Cas confirmés',
      data: this.casConf
    }, {
      name: 'Guéris',
      data: this.gueris
    }, {
      name: 'Hospitalisés',
      data: this.hospi
    }, {
      name: 'Décès',
      data: this.deces
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
  }

  ngOnChanges(composant: SimpleChanges ){
    if (this.locationName) {
      if (composant.isRegion) {
        this.getRegInfos();
      } else {
        this.getDeptInfos();
      }
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
      }
    );
  }

  getDeptInfos() {
    this.mapService.getInfosDept(this.actualdate, this.locationName).subscribe(
      data => {
        this.deptList = data;
      }
    );
  }

  getHFrance(){
    this.historiqueService.getHistoriqueFrance().subscribe(
      data => {
        this.histFr = data;
        this.setAllDataFromFrance(this.histFr);
      }
    );
  }

  // Get Data from list and put it in this.allData
  // Order this.allData by Date
  // set Graph
  setAllDataFromFrance(list) {
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
    for (const index in list) {
      this.dates.push(list[index].date);
      this.casConf.push(list[index].totalCases);
      this.hospi.push(list[index].hospitalized);
      this.deces.push(list[index].totalDeaths);
      this.gueris.push(list[index].recoveredCases);
    }
  }


}
