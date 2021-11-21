import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBlackList } from 'app/shared/model/black-list.model';

type EntityResponseType = HttpResponse<IBlackList>;
type EntityArrayResponseType = HttpResponse<IBlackList[]>;

@Injectable({ providedIn: 'root' })
export class BlackListService {
  public resourceUrl = SERVER_API_URL + 'api/black-lists';

  constructor(protected http: HttpClient) {}

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBlackList[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((blackList: IBlackList) => {
        blackList.unlockDate = blackList.unlockDate ? moment(blackList.unlockDate) : undefined;
        blackList.lastTry = blackList.lastTry ? moment(blackList.lastTry) : undefined;
      });
    }
    return res;
  }
}
