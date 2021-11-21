import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBlackList } from 'app/shared/model/black-list.model';
import { BlackListService } from './black-list.service';

@Component({
  templateUrl: './black-list-delete-dialog.component.html',
})
export class BlackListDeleteDialogComponent {
  blackList?: IBlackList;

  constructor(protected blackListService: BlackListService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.blackListService.delete(id).subscribe(() => {
      this.eventManager.broadcast('blackListListModification');
      this.activeModal.close();
    });
  }
}
