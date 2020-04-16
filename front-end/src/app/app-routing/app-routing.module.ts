import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MenuComponent } from './../components/menu/menu.component';
import { MapComponent } from './../components/map/map.component';
import {SimulationComponent} from '../components/simulation/simulation.component';

const routes: Routes = [
  {
    path: '',
    component: MenuComponent,
  },
  {
    path: 'map',
    component: MapComponent,
  },
  {
    path: 'simulation',
    component: SimulationComponent,
  },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ],
  declarations: []
})

export class AppRoutingModule { }
