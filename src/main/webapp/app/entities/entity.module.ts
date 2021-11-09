import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'electro-vanne',
        data: { pageTitle: 'arrosageApp.projet.home.title' },
        loadChildren: () => import('./electro-vanne/electro-vanne.module').then(m => m.ElectroVanneModule),
      },
    ]),
  ],
})
export class ArrosageEntityModule {}
