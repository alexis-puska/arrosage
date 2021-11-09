import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { StatusRelay } from './status-relay.model';

@Injectable({ providedIn: 'root' })
export class ElectroVanneService {
  public resourceUrl = SERVER_API_URL + 'api/electro-vanne';

  constructor(private http: HttpClient) {}

  open(relay: number): Observable<void> {
    return this.http.get<void>(`${this.resourceUrl}/open/${relay}`);
  }

  close(relay: number): Observable<void> {
    return this.http.get<void>(`${this.resourceUrl}/close/${relay}`);
  }

  status(): Observable<HttpResponse<StatusRelay[]>> {
    return this.http.get<StatusRelay[]>(`${this.resourceUrl}/status`, { observe: 'response' });
  }

  cancelAll(): Observable<void> {
    return this.http.get<void>(`${this.resourceUrl}/cancel-all`);
  }
}
