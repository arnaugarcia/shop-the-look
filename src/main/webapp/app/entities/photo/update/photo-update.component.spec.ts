jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PhotoService } from '../service/photo.service';
import { IPhoto, Photo } from '../photo.model';
import { ISpace } from 'app/entities/space/space.model';
import { SpaceService } from 'app/entities/space/service/space.service';
import { ISpaceTemplate } from 'app/entities/space-template/space-template.model';
import { SpaceTemplateService } from 'app/entities/space-template/service/space-template.service';

import { PhotoUpdateComponent } from './photo-update.component';

describe('Component Tests', () => {
  describe('Photo Management Update Component', () => {
    let comp: PhotoUpdateComponent;
    let fixture: ComponentFixture<PhotoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let photoService: PhotoService;
    let spaceService: SpaceService;
    let spaceTemplateService: SpaceTemplateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PhotoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PhotoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PhotoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      photoService = TestBed.inject(PhotoService);
      spaceService = TestBed.inject(SpaceService);
      spaceTemplateService = TestBed.inject(SpaceTemplateService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Space query and add missing value', () => {
        const photo: IPhoto = { id: 456 };
        const space: ISpace = { id: 73617 };
        photo.space = space;

        const spaceCollection: ISpace[] = [{ id: 64414 }];
        jest.spyOn(spaceService, 'query').mockReturnValue(of(new HttpResponse({ body: spaceCollection })));
        const additionalSpaces = [space];
        const expectedCollection: ISpace[] = [...additionalSpaces, ...spaceCollection];
        jest.spyOn(spaceService, 'addSpaceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ photo });
        comp.ngOnInit();

        expect(spaceService.query).toHaveBeenCalled();
        expect(spaceService.addSpaceToCollectionIfMissing).toHaveBeenCalledWith(spaceCollection, ...additionalSpaces);
        expect(comp.spacesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call SpaceTemplate query and add missing value', () => {
        const photo: IPhoto = { id: 456 };
        const spaceTemplate: ISpaceTemplate = { id: 9298 };
        photo.spaceTemplate = spaceTemplate;

        const spaceTemplateCollection: ISpaceTemplate[] = [{ id: 39222 }];
        jest.spyOn(spaceTemplateService, 'query').mockReturnValue(of(new HttpResponse({ body: spaceTemplateCollection })));
        const additionalSpaceTemplates = [spaceTemplate];
        const expectedCollection: ISpaceTemplate[] = [...additionalSpaceTemplates, ...spaceTemplateCollection];
        jest.spyOn(spaceTemplateService, 'addSpaceTemplateToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ photo });
        comp.ngOnInit();

        expect(spaceTemplateService.query).toHaveBeenCalled();
        expect(spaceTemplateService.addSpaceTemplateToCollectionIfMissing).toHaveBeenCalledWith(
          spaceTemplateCollection,
          ...additionalSpaceTemplates
        );
        expect(comp.spaceTemplatesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const photo: IPhoto = { id: 456 };
        const space: ISpace = { id: 78169 };
        photo.space = space;
        const spaceTemplate: ISpaceTemplate = { id: 23681 };
        photo.spaceTemplate = spaceTemplate;

        activatedRoute.data = of({ photo });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(photo));
        expect(comp.spacesSharedCollection).toContain(space);
        expect(comp.spaceTemplatesSharedCollection).toContain(spaceTemplate);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Photo>>();
        const photo = { id: 123 };
        jest.spyOn(photoService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ photo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: photo }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(photoService.update).toHaveBeenCalledWith(photo);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Photo>>();
        const photo = new Photo();
        jest.spyOn(photoService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ photo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: photo }));
        saveSubject.complete();

        // THEN
        expect(photoService.create).toHaveBeenCalledWith(photo);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Photo>>();
        const photo = { id: 123 };
        jest.spyOn(photoService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ photo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(photoService.update).toHaveBeenCalledWith(photo);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSpaceById', () => {
        it('Should return tracked Space primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSpaceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackSpaceTemplateById', () => {
        it('Should return tracked SpaceTemplate primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSpaceTemplateById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
