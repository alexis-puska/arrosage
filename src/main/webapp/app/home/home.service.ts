import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { Metric } from '../shared/model/metric.model';

@Injectable({ providedIn: 'root' })
export class HomeService {
  public resourceUrl = SERVER_API_URL + 'api/home';

  constructor(protected http: HttpClient) {}

  metrics(): Observable<Metric> {
    return this.http.get<Metric>(`${this.resourceUrl}/metrics`);
  }
}
