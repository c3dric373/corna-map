import { Component, OnInit } from '@angular/core';
import {SideBarLeftService} from '../../side-bar-left.service';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css']
})
export class SideBarComponent implements OnInit {


  constructor(private leftService: SideBarLeftService ) {}

  nbCC = 548;
  nbG = 444;
  nbH = 128;
  nbD = 185;

  donnees ;

  ngOnInit(): void {
    this.essaiMap();
  }
  essaiMap() {
    this.leftService.getMap().subscribe(
      data => {
        this.donnees = data;
        console.log(data);
      }
    );


}}
