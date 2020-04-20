import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MapService {

  API_URL =  'http://127.0.0.1:8080/';

  constructor(private httpClient: HttpClient) { }

  getMap(): Observable<any>{
    return this.httpClient.get(this.API_URL + 'testday?paramName=france' );
  }

  getMapRegion(): Observable<any>{
    return this.httpClient.get(this.API_URL + 'testday?paramName=region' );
  }

  getMapDept(): Observable<any>{
    return this.httpClient.get(this.API_URL + 'testday?paramName=dept' );
  }

}
