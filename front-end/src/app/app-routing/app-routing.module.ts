import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MapMenuComponent } from '../components/map-menu/map-menu.component';
import {SimulationComponent} from '../components/simulation/simulation.component';

const routes: Routes = [
  {
    path: '',
    component: MapMenuComponent,
  },
  {
    path: 'carte',
    component: MapMenuComponent,
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
