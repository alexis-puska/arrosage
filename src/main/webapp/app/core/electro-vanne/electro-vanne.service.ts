import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { Relay } from 'app/shared/model/relay.model';
import { Observable } from 'rxjs';
import { StatusRelay } from './status-relay.model';

@Injectable({ providedIn: 'root' })
export class ElectroVanneService {
  public resourceUrl = SERVER_API_URL + 'api/electro-vanne';

  constructor(private http: HttpClient) {}

  open(id: number): Observable<void> {
    return this.http.get<void>(`${this.resourceUrl}/open/${id}`);
  }

  close(id: number): Observable<void> {
    return this.http.get<void>(`${this.resourceUrl}/close/${id}`);
  }

  addTime(id: number): Observable<void> {
    return this.http.get<void>(`${this.resourceUrl}/add-time/${id}`);
  }

  cancel(id: number): Observable<void> {
    return this.http.get<void>(`${this.resourceUrl}/cancel/${id}`);
  }

  status(): Observable<HttpResponse<StatusRelay[]>> {
    return this.http.get<StatusRelay[]>(`${this.resourceUrl}/status`, { observe: 'response' });
  }

  cancelAll(): Observable<void> {
    return this.http.get<void>(`${this.resourceUrl}/cancel-all`);
  }

  listRelay(): Observable<Relay[]> {
    return this.http.get<Relay[]>(`${this.resourceUrl}/list-relay`);
  }
}
