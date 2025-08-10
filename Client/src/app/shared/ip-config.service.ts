import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ipConfigService {

  constructor() { }

  getAddress(): string {
    // return 'http://localhost:7878';
    return 'http://94.250.203.197:7878';
  }

}
