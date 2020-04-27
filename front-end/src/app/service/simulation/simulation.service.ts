import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NgbDate} from '@ng-bootstrap/ng-bootstrap';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SimulationService {

  API_URL =  'http://127.0.0.1:8080/';

  constructor(private httpClient: HttpClient) { }


  getInfosFrance(date: NgbDate): Observable<any>{
    const stringDate = this.dateToString(date);
    return this.httpClient.get(this.API_URL + 'simulation/infosFrance?date=' + date );
  }

  getInfosRegion(date: NgbDate, name: string): Observable<any>{
    const stringDate = this.dateToString(date);
    return this.httpClient.get(this.API_URL + 'simulation/infosRegion?date=' + date + '&name=' + name );
    // si le nom est null, retourner les données de toutes les régions
  }

  getInfosDept(date: NgbDate, name: string): Observable<any>{
    const stringDate = this.dateToString(date);
    return this.httpClient.get(this.API_URL + 'simulation/infosDept?date=' + date + '&name=' + name );
    // si le nom est null, retourner les données de tout les départements
  }

  dateToString(date: NgbDate): string {
    let dateString = '' + date.year;
    if (date.month < 10 ){
      dateString = dateString + '-0' + date.month;
    } else {
      dateString = dateString + '-' + date.month;
    }
    if (date.day < 10 ){
      dateString = dateString + '-0' + date.day;
    } else {
      dateString = dateString + '-' + date.day;
    }
    return dateString;
  }
}
