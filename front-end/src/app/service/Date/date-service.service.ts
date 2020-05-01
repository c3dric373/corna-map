import { Injectable } from '@angular/core';
import {NgbDate} from '@ng-bootstrap/ng-bootstrap';

@Injectable({
  providedIn: 'root'
})
export class DateServiceService {

  constructor() { }

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

  formatDateToFr(date: NgbDate): string {
    let dateString = '';
    if (date.day < 10 ){
      dateString = dateString + '0' + date.day;
    } else {
      dateString = dateString + date.day;
    }
    if (date.month < 10 ){
      dateString = dateString + '/0' + date.month;
    } else {
      dateString = dateString + '/' + date.month;
    }
    dateString = dateString + '/' + date.year;
    return dateString;
  }
}
