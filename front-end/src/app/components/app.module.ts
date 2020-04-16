import { BrowserModule } from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './../app-routing/app-routing.module';

import { AppComponent } from './app.component';
import { MapComponent } from './map/map.component';
import { MenuComponent } from './menu/menu.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { SideBarComponent } from './side-bar/side-bar.component';
import { RightBarComponent } from './right-bar/right-bar.component';
import { SimulationComponent } from './simulation/simulation.component';
import { LeftSimulationComponent } from './left-simulation/left-simulation.component';


@NgModule({
  declarations: [
    AppComponent,
    MapComponent,
    MenuComponent,
    NavBarComponent,
    SideBarComponent,
    RightBarComponent,
    SimulationComponent,
    LeftSimulationComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
