import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ipConfigService {

  constructor() { }

  getAddress(): string {
    return 'https://training.grs.gov.bd/helpdesk';
    // return 'http://94.250.203.197:7890/helpdesk';
  }

}
