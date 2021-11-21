import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ArrosageSharedModule } from 'app/shared/shared.module';
import { BlackListDeleteDialogComponent } from './black-list-delete-dialog.component';
import { BlackListComponent } from './black-list.component';
import { blackListRoute } from './black-list.route';

@NgModule({
  imports: [ArrosageSharedModule, RouterModule.forChild(blackListRoute)],
  declarations: [BlackListComponent, BlackListDeleteDialogComponent],
  entryComponents: [BlackListDeleteDialogComponent],
})
export class ArrosageBlackListModule {}
