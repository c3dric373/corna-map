import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css']
})
export class SideBarComponent implements OnInit {

  constructor() { }

  nbCC = 548;
  nbG = 444;
  nbH = 128;
  nbD = 185;

  ngOnInit(): void {
  }

}
