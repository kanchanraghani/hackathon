import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  url = 'http://localhost:9696/submitrequest';
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  sendDetails(clientRequest) {
    return this.http.post(this.url, clientRequest, this.httpOptions);
  }
}
