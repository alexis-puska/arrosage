import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProgrammation } from 'app/shared/model/programmation.model';
import { ProgrammationService } from './programmation.service';

@Component({
  templateUrl: './programmation-delete-dialog.component.html',
})
export class ProgrammationDeleteDialogComponent {
  programmation?: IProgrammation;

  constructor(
    protected programmationService: ProgrammationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.programmationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('programmationListModification');
      this.activeModal.close();
    });
  }
}
