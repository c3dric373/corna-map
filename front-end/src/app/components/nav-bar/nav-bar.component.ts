import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {

  constructor() { }

  act = false;

  ngOnInit(): void {
    
  }

  changeAct(){
    this.act = !this.act;
    console.log('clickkkkkk');
  }
}
