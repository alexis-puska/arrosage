import { Routes } from '@angular/router';
import { ElectroVanneComponent } from './electro-vanne.component';

/* @Injectable({ providedIn: 'root' })
export class UserManagementResolve implements Resolve<IUser> {
  constructor(private service: UserService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUser> {
    const id = route.params['login'];
    if (id) {
      return this.service.find(id);
    }
    return of(new User());
  }
}
 */
export const electroVanneRoute: Routes = [
  {
    path: '',
    component: ElectroVanneComponent,
  },
];
