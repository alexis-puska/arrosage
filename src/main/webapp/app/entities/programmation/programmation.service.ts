import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IProgrammation } from 'app/shared/model/programmation.model';

type EntityResponseType = HttpResponse<IProgrammation>;
type EntityArrayResponseType = HttpResponse<IProgrammation[]>;

@Injectable({ providedIn: 'root' })
export class ProgrammationService {
  public resourceUrl = SERVER_API_URL + 'api/programmations';

  constructor(protected http: HttpClient) {}

  create(programmation: IProgrammation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(programmation);
    return this.http
      .post<IProgrammation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(programmation: IProgrammation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(programmation);
    return this.http
      .put<IProgrammation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProgrammation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProgrammation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(programmation: IProgrammation): IProgrammation {
    const copy: IProgrammation = Object.assign({}, programmation, {
      date: programmation.date && programmation.date.isValid() ? programmation.date.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? moment(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((programmation: IProgrammation) => {
        programmation.date = programmation.date ? moment(programmation.date) : undefined;
      });
    }
    return res;
  }
}
