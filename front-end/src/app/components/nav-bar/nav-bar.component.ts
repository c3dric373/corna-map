import { Component, OnInit } from '@angular/core';
import {NavigationEnd, Router} from '@angular/router';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {
  public  isMap: boolean;
  private url: string;

  constructor(private router: Router) {
    // On routes changes
    router.events.subscribe((val) => {
      if (val instanceof  NavigationEnd) {
        this.url = router.url;
        if (this.url === '/simulation') {
          this.changeSimul();
        } else {
          this.changeMap();
        }
      }
    });
  }

  ngOnInit(): void {
  }

  changeMap(){
      this.isMap = true;
  }

  changeSimul(){
    this.isMap = false;
  }
}
