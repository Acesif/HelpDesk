import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ipConfigService {

  constructor() { }

  getAddress(): string {
    return 'https://training.grs.gov.bd/helpdeskserver';
    // return 'http://94.250.203.197:7878';
  }

}
