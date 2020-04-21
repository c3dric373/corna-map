import {Component, Input, OnInit} from '@angular/core';
import {MapService} from '../../service/map/map.service';
import {NgbDate} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css']
})
export class SideBarComponent implements OnInit {
  donnees ;
  @Input() date: NgbDate;

  constructor(private mapService: MapService) {}


  ngOnInit(): void {
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
