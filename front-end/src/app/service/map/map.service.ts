import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {NgbDate} from '@ng-bootstrap/ng-bootstrap';
import {DateServiceService} from '../Date/date-service.service';

@Injectable({
  providedIn: 'root'
})
export class MapService {

  API_URL =  'http://127.0.0.1:8080/';

  constructor(private httpClient: HttpClient, private dateService: DateServiceService) { }

  getInfosFrance(date: NgbDate): Observable<any>{
    const stringDate = this.dateService.dateToString(date);
    return this.httpClient.get(this.API_URL + 'map/infosFrance?date=2020-04-18' );
  }

  getMapRegion(date): Observable<any>{
    const stringDate = this.dateService.dateToString(date);
    return this.httpClient.get(this.API_URL + 'map/infosRegion?date=' + stringDate );
  }

  getMapDept(date: NgbDate): Observable<any>{
    const stringDate = this.dateService.dateToString(date);
    return this.httpClient.get(this.API_URL + 'map/infosDept?date=' + stringDate );
  }

  getInfosRegion(date: NgbDate, name: string): Observable<any>{
    const stringDate = this.dateService.dateToString(date);
    return this.httpClient.get(this.API_URL + 'map/infosRegion?date=2020-04-27&name=' + name );
  }

  getInfosDept(date: NgbDate, name: string): Observable<any>{
    const stringDate = this.dateService.dateToString(date);
    return this.httpClient.get(this.API_URL + 'map/infosDept?date=2020-04-27&name=' + name );
  }



}
