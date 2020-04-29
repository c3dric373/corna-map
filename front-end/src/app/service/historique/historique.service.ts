import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class HistoriqueService {
  API_URL =  'http://127.0.0.1:8080/';

  constructor(private httpClient: HttpClient) { }

  getHistoriqueFrance(): Observable<any>{
    return this.httpClient.get(this.API_URL + 'historique?location=FRA' );
  }

  getHistoriqueRegion(name): Observable<any>{
    return this.httpClient.get(this.API_URL + 'historique?location=region&name=' + name );
  }

  getHistoriqueDept(name): Observable<any>{
    return this.httpClient.get(this.API_URL + 'historique?location=dept&name=' + name );
  }

}
