import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ArrosageTestModule } from '../../../test.module';
import { BlackListDetailComponent } from 'app/entities/black-list/black-list-detail.component';
import { BlackList } from 'app/shared/model/black-list.model';

describe('Component Tests', () => {
  describe('BlackList Management Detail Component', () => {
    let comp: BlackListDetailComponent;
    let fixture: ComponentFixture<BlackListDetailComponent>;
    const route = ({ data: of({ blackList: new BlackList(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ArrosageTestModule],
        declarations: [BlackListDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(BlackListDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BlackListDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load blackList on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.blackList).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
