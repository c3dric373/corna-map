import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class MapService {

  API_URL =  'http://127.0.0.1:5000/';

  constructor(){} // private httpClient: HttpClient) { }

  getMap(): string {
    return 'maaaaaap'; // this.httpClient.get(this.API_URL + 'map/essai');
  }
}
