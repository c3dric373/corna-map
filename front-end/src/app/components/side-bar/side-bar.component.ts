import {Component, Input, OnInit} from '@angular/core';
import {MapService} from '../../service/map/map.service';
import {NgbCalendar, NgbDate} from '@ng-bootstrap/ng-bootstrap';
import {InfosFrance} from '../../model/infosFrance';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css']
})
export class SideBarComponent implements OnInit {
  public donnees: InfosFrance;
  @Input() date: NgbDate;

  constructor(private mapService: MapService, calendar: NgbCalendar) {
    this.date = calendar.getToday();
  }

  ngOnInit(): void {
    this.donnees = new InfosFrance();
    this.getInfosFrance();
  }

  getInfosFrance() {
    this.mapService.getInfosFrance(this.date).subscribe(
      data => {
        this.donnees = data;
        console.log(data);
      }
    );
}

}
