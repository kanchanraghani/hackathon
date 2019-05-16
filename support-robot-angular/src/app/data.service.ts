import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  _url = 'http://localhost:8080/submitrequest';

  constructor(private http: HttpClient) { }

  sendDetails(clientRequest){
    return this.http.post<any>(this._url, clientRequest);
}
}
