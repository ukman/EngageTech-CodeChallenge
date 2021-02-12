import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
providedIn: 'root'
})
export class VatService {

private vatsUrl = 'http://localhost:8000/api/vats';

httpOptions = {
headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  getVat(country: string): Observable<number> {
    return this.http.get<number>(this.vatsUrl + "/" + country);
  }

}
