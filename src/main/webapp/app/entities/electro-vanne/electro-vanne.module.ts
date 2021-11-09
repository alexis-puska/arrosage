import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ArrosageSharedModule } from 'app/shared/shared.module';
import { ElectroVanneComponent } from './electro-vanne.component';
import { electroVanneRoute } from './electro-vanne.route';

@NgModule({
  imports: [ArrosageSharedModule, RouterModule.forChild(electroVanneRoute)],
  declarations: [ElectroVanneComponent],
  entryComponents: [],
})
export class ElectroVanneModule {}
