import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Authority } from 'app/shared/constants/authority.constants';
import { BlackListComponent } from './black-list.component';

export const blackListRoute: Routes = [
  {
    path: '',
    component: BlackListComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'BlackLists',
    },
    canActivate: [UserRouteAccessService],
  }
];
