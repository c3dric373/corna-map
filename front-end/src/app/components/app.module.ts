import { BrowserModule } from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './../app-routing/app-routing.module';

import { AppComponent } from './app.component';
import { MapMenuComponent } from './map-menu/map-menu.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { SideBarComponent } from './side-bar/side-bar.component';
import { RightBarComponent } from './right-bar/right-bar.component';
import { SimulationComponent } from './simulation/simulation.component';
import { LeftSimulationComponent } from './left-simulation/left-simulation.component';
import { MapContentComponent } from './map-content/map-content.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { LoadingSpinComponent } from './loading-spin/loading-spin.component';
import {NgbDropdownModule, NgbModule} from '@ng-bootstrap/ng-bootstrap';


@NgModule({
  declarations: [
    AppComponent,
    MapMenuComponent,
    NavBarComponent,
    SideBarComponent,
    RightBarComponent,
    SimulationComponent,
    LeftSimulationComponent,
    MapContentComponent,
    LoadingSpinComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FontAwesomeModule,
    NgbDropdownModule,
    NgbModule,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
