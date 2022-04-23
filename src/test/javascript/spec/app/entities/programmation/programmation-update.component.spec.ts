import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ArrosageTestModule } from '../../../test.module';
import { ProgrammationUpdateComponent } from 'app/entities/programmation/programmation-update.component';
import { ProgrammationService } from 'app/entities/programmation/programmation.service';
import { Programmation } from 'app/shared/model/programmation.model';

describe('Component Tests', () => {
  describe('Programmation Management Update Component', () => {
    let comp: ProgrammationUpdateComponent;
    let fixture: ComponentFixture<ProgrammationUpdateComponent>;
    let service: ProgrammationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ArrosageTestModule],
        declarations: [ProgrammationUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ProgrammationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProgrammationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProgrammationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Programmation(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Programmation();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
