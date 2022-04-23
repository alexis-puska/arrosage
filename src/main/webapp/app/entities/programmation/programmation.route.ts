import { HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ElectroVanneService } from 'app/core/electro-vanne/electro-vanne.service';
import { Authority } from 'app/shared/constants/authority.constants';
import { IProgrammation, Programmation } from 'app/shared/model/programmation.model';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { Relay } from './../../shared/model/relay.model';
import { ProgrammationDetailComponent } from './programmation-detail.component';
import { ProgrammationUpdateComponent } from './programmation-update.component';
import { ProgrammationComponent } from './programmation.component';
import { ProgrammationService } from './programmation.service';

@Injectable({ providedIn: 'root' })
export class ProgrammationResolve implements Resolve<IProgrammation> {
  constructor(private service: ProgrammationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProgrammation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((programmation: HttpResponse<Programmation>) => {
          if (programmation.body) {
            return of(programmation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Programmation());
  }
}

@Injectable({ providedIn: 'root' })
export class ListRelayResolve implements Resolve<Relay[]> {
  constructor(private service: ElectroVanneService) {}
  resolve(): Observable<Relay[]> {
    return this.service.listRelay();
  }
}

export const programmationRoute: Routes = [
  {
    path: '',
    component: ProgrammationComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Programmations',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProgrammationDetailComponent,
    resolve: {
      programmation: ProgrammationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Programmations',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProgrammationUpdateComponent,
    resolve: {
      programmation: ProgrammationResolve,
      listRelay: ListRelayResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Programmations',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProgrammationUpdateComponent,
    resolve: {
      programmation: ProgrammationResolve,
      listRelay: ListRelayResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Programmations',
    },
    canActivate: [UserRouteAccessService],
  },
];
