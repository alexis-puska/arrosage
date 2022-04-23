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
      {
        path: 'black-list',
        data: { pageTitle: 'arrosageApp.projet.home.title' },
        loadChildren: () => import('./black-list/black-list.module').then(m => m.ArrosageBlackListModule),
      },
      {
        path: 'programmation',
        data: { pageTitle: 'arrosageApp.projet.home.title' },
        loadChildren: () => import('./programmation/programmation.module').then(m => m.ArrosageProgrammationModule),
      },
    ]),
  ],
})
export class ArrosageEntityModule {}
