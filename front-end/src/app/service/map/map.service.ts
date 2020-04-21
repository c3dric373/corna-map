import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MapService {

  API_URL =  'http://127.0.0.1:8080/';

  constructor(private httpClient: HttpClient) { }

  getInfosFrance(date): Observable<any>{
    const stringDate = this.dateToString(date);
    return this.httpClient.get(this.API_URL + 'map/infosFrance?date=2020-04-20' );
  }

  getMapRegion(date): Observable<any>{
    const stringDate = this.dateToString(date);
    return this.httpClient.get(this.API_URL + 'map/infosRegion?date=2020-04-20' );
  }

  getMapDept(date): Observable<any>{
    const stringDate = this.dateToString(date);
    return this.httpClient.get(this.API_URL + 'map/infosDept?date=2020-04-20' );
  }

  dateToString(date): string {
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
