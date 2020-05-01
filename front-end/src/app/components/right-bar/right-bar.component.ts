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

  public showLocation = false;

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
    this.actualdate = calendar.getToday();
  }

  ngOnInit() {
    Highcharts.chart('charts', this.options);
    this.getHFrance();
    this.showLocation = false;
    this.locationName = 'France';
  }

  ngOnChanges(composant: SimpleChanges ){
    if (this.locationName !== 'France') {
      if (this.isRegion) {
        this.getRegInfos();
      } else {
        this.getDeptInfos();
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
        this.locationName = data.nom;
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
        this.locationName = data.nom;
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
        this.histFr = data;
        console.log(this.histFr);
        for (const i in this.histFr){
          console.log(this.histFr[i]);
          this.casConf.push(this.histFr[i].totalCases);
          this.dates.push(this.histFr[i].date.month);
          // this.histFr[i].date.

        }
        Highcharts.chart('charts', this.options);
      }
    );

  }


}
