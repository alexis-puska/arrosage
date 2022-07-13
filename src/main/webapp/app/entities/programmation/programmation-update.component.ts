import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IProgrammation, Programmation } from 'app/shared/model/programmation.model';
import { ProgrammationService } from './programmation.service';
import { Relay } from 'app/shared/model/relay.model';

@Component({
  selector: 'jhi-programmation-update',
  templateUrl: './programmation-update.component.html',
})
export class ProgrammationUpdateComponent implements OnInit {
  isSaving = false;
  listRelay: Relay[] = [];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    day: [1, [Validators.required, Validators.max(30), Validators.min(1)]],
    sequence: [null, [Validators.required, Validators.pattern('^(?!,$)[\\d,]+$')]],
    counter: [],
    dayFrequency: [1, [Validators.required, Validators.max(30), Validators.min(1)]],
  });

  constructor(protected programmationService: ProgrammationService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ programmation, listRelay }) => {
      this.listRelay = listRelay;
      if (!programmation.id) {
        const today = moment().startOf('day');
        programmation.date = today;
      }

      this.updateForm(programmation);
    });
  }

  updateForm(programmation: IProgrammation): void {
    this.editForm.patchValue({
      id: programmation.id,
      date: programmation.date ? programmation.date.format(DATE_TIME_FORMAT) : null,
      day: programmation.day,
      sequence: programmation.sequence,
      counter: programmation.counter,
      dayFrequency: programmation.dayFrequency,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const programmation = this.createFromForm();
    if (programmation.id !== undefined) {
      this.subscribeToSaveResponse(this.programmationService.update(programmation));
    } else {
      this.subscribeToSaveResponse(this.programmationService.create(programmation));
    }
  }

  addToSequence(id?: number): void {
    if (id) {
      let seq = this.editForm.get('sequence')?.value;
      if (!seq || seq.length === 0) {
        seq = id.toString();
      } else {
        seq = seq + ',' + id.toString();
      }
      this.editForm.patchValue({ sequence: seq });
    }
  }

  clearSequence(): void {
    this.editForm.patchValue({ sequence: '' });
  }

  getSequence(): string {
    return this.editForm.get('sequence')?.value;
  }

  getCountInSequence(id?: number): number {
    const n = this.editForm.get('sequence')?.value;
    if (n && id) {
      return (n.match(new RegExp(id.toString(), 'g')) || []).length;
    }
    return 0;
  }

  private createFromForm(): IProgrammation {
    return {
      ...new Programmation(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      day: this.editForm.get(['day'])!.value,
      sequence: this.editForm.get(['sequence'])!.value,
      counter: this.editForm.get(['counter'])!.value,
      dayFrequency: this.editForm.get(['dayFrequency'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProgrammation>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
