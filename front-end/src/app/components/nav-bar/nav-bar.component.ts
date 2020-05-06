import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {
  public  isMap: boolean;
  private url: string;

  constructor(private router: Router) { }

  ngOnInit(): void {
    this.isMap = true;
  }

  changeMap(){
      this.isMap = true;
  }

  changeSimul(){
    this.isMap = false;
  }
}
