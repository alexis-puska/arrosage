import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { BlackListService } from 'app/entities/black-list/black-list.service';
import { IBlackList, BlackList } from 'app/shared/model/black-list.model';

describe('Service Tests', () => {
  describe('BlackList Service', () => {
    let injector: TestBed;
    let service: BlackListService;
    let httpMock: HttpTestingController;
    let elemDefault: IBlackList;
    let expectedResult: IBlackList | IBlackList[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(BlackListService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new BlackList(0, 'AAAAAAA', false, currentDate, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            unlockDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a BlackList', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            unlockDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            unlockDate: currentDate,
          },
          returnedFromService
        );

        service.create(new BlackList()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a BlackList', () => {
        const returnedFromService = Object.assign(
          {
            ipAddress: 'BBBBBB',
            locked: true,
            unlockDate: currentDate.format(DATE_TIME_FORMAT),
            count: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            unlockDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of BlackList', () => {
        const returnedFromService = Object.assign(
          {
            ipAddress: 'BBBBBB',
            locked: true,
            unlockDate: currentDate.format(DATE_TIME_FORMAT),
            count: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            unlockDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a BlackList', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
