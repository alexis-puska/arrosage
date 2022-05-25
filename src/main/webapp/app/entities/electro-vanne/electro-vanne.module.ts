import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ArrosageSharedModule } from 'app/shared/shared.module';
import { CountdownModule } from 'ngx-countdown';
import { ElectroVanneComponent } from './electro-vanne.component';
import { electroVanneRoute } from './electro-vanne.route';

@NgModule({
  imports: [ArrosageSharedModule, CountdownModule, RouterModule.forChild(electroVanneRoute)],
  declarations: [ElectroVanneComponent],
  entryComponents: [],
})
export class ElectroVanneModule {}
