import {Component, Input, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';
import {NgbDate} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-right-bar',
  templateUrl: './right-bar.component.html',
  styleUrls: ['./right-bar.component.css']
})
export class RightBarComponent implements OnInit {
  @Input() locationName: string;
  @Input() isRegion: boolean;
  @Input() actualdate: NgbDate;

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
      categories: ['1750', '1800', '1850', '1900', '1950', '1999', '2050'],
      tickmarkPlacement: 'on',
      title: {
        enabled: false
      }
    },
    series: [{
      name: 'Cas confirmés',
      data: [502, 635, 809, 947, 1402, 3634, 5268]
    }, {
      name: 'Guéris',
      data: [163, 203, 276, 408, 547, 729, 628]
    }, {
      name: 'Hospitalisés',
      data: [10, 31, 50, 156, 339, 300, 280]
    }, {
      name: 'Décès',
      data: [18, 31, 54, 156, 339, 818, 1201]
    }
    ]
  };

  constructor() { }

  ngOnInit() {
    Highcharts.chart('click', this.options);
  }

}
