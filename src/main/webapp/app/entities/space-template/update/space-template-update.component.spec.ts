jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SpaceTemplateService } from '../service/space-template.service';
import { ISpaceTemplate, SpaceTemplate } from '../space-template.model';

import { SpaceTemplateUpdateComponent } from './space-template-update.component';

describe('Component Tests', () => {
  describe('SpaceTemplate Management Update Component', () => {
    let comp: SpaceTemplateUpdateComponent;
    let fixture: ComponentFixture<SpaceTemplateUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let spaceTemplateService: SpaceTemplateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SpaceTemplateUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SpaceTemplateUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SpaceTemplateUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      spaceTemplateService = TestBed.inject(SpaceTemplateService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const spaceTemplate: ISpaceTemplate = { id: 456 };

        activatedRoute.data = of({ spaceTemplate });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(spaceTemplate));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SpaceTemplate>>();
        const spaceTemplate = { id: 123 };
        jest.spyOn(spaceTemplateService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ spaceTemplate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: spaceTemplate }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(spaceTemplateService.update).toHaveBeenCalledWith(spaceTemplate);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SpaceTemplate>>();
        const spaceTemplate = new SpaceTemplate();
        jest.spyOn(spaceTemplateService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ spaceTemplate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: spaceTemplate }));
        saveSubject.complete();

        // THEN
        expect(spaceTemplateService.create).toHaveBeenCalledWith(spaceTemplate);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SpaceTemplate>>();
        const spaceTemplate = { id: 123 };
        jest.spyOn(spaceTemplateService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ spaceTemplate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(spaceTemplateService.update).toHaveBeenCalledWith(spaceTemplate);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
