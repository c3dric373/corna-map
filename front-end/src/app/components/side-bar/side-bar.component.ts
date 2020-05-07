import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {MapService} from '../../service/map/map.service';
import {NgbCalendar, NgbDate} from '@ng-bootstrap/ng-bootstrap';
import {InfosFrance} from '../../model/infosFrance';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css']
})
export class SideBarComponent implements OnInit, OnChanges {
  public donnees: InfosFrance;
  @Input() date: NgbDate;

  constructor(private mapService: MapService, calendar: NgbCalendar) {
    // set today's date
    this.date = calendar.getToday();
    // set date to 2 days before today
    this.date = calendar.getPrev(this.date, 'd', 2);
  }

  ngOnInit(): void {
    this.donnees = new InfosFrance();
    this.getInfosFrance();
  }

  ngOnChanges(composant: SimpleChanges ){
    this.getInfosFrance();
  }

  getInfosFrance() {
    this.mapService.getInfosFrance(this.date).subscribe(
      data => {
        this.donnees = data;
      }
    );
}

}
