import { Component, OnInit } from '@angular/core';
import { faStop } from '@fortawesome/free-solid-svg-icons';
import { faPlay } from '@fortawesome/free-solid-svg-icons';
import { faPause } from '@fortawesome/free-solid-svg-icons';


@Component({
  selector: 'app-left-simulation',
  templateUrl: './left-simulation.component.html',
  styleUrls: ['./left-simulation.component.css']
})
export class LeftSimulationComponent implements OnInit {


  constructor() { }
  faStop = faStop;
  faPlay = faPlay;
  faPause = faPause;


    ngOnInit(): void {
  }






}
