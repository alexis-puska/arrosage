import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProgrammation } from 'app/shared/model/programmation.model';

@Component({
  selector: 'jhi-programmation-detail',
  templateUrl: './programmation-detail.component.html',
})
export class ProgrammationDetailComponent implements OnInit {
  programmation: IProgrammation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ programmation }) => (this.programmation = programmation));
  }

  previousState(): void {
    window.history.back();
  }
}
