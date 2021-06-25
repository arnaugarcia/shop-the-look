jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CoordinateService } from '../service/coordinate.service';
import { ICoordinate, Coordinate } from '../coordinate.model';
import { IPhoto } from 'app/entities/photo/photo.model';
import { PhotoService } from 'app/entities/photo/service/photo.service';

import { CoordinateUpdateComponent } from './coordinate-update.component';

describe('Component Tests', () => {
  describe('Coordinate Management Update Component', () => {
    let comp: CoordinateUpdateComponent;
    let fixture: ComponentFixture<CoordinateUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let coordinateService: CoordinateService;
    let photoService: PhotoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CoordinateUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CoordinateUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CoordinateUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      coordinateService = TestBed.inject(CoordinateService);
      photoService = TestBed.inject(PhotoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Photo query and add missing value', () => {
        const coordinate: ICoordinate = { id: 456 };
        const photo: IPhoto = { id: 99010 };
        coordinate.photo = photo;

        const photoCollection: IPhoto[] = [{ id: 16149 }];
        jest.spyOn(photoService, 'query').mockReturnValue(of(new HttpResponse({ body: photoCollection })));
        const additionalPhotos = [photo];
        const expectedCollection: IPhoto[] = [...additionalPhotos, ...photoCollection];
        jest.spyOn(photoService, 'addPhotoToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ coordinate });
        comp.ngOnInit();

        expect(photoService.query).toHaveBeenCalled();
        expect(photoService.addPhotoToCollectionIfMissing).toHaveBeenCalledWith(photoCollection, ...additionalPhotos);
        expect(comp.photosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const coordinate: ICoordinate = { id: 456 };
        const photo: IPhoto = { id: 76152 };
        coordinate.photo = photo;

        activatedRoute.data = of({ coordinate });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(coordinate));
        expect(comp.photosSharedCollection).toContain(photo);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Coordinate>>();
        const coordinate = { id: 123 };
        jest.spyOn(coordinateService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ coordinate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: coordinate }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(coordinateService.update).toHaveBeenCalledWith(coordinate);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Coordinate>>();
        const coordinate = new Coordinate();
        jest.spyOn(coordinateService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ coordinate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: coordinate }));
        saveSubject.complete();

        // THEN
        expect(coordinateService.create).toHaveBeenCalledWith(coordinate);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Coordinate>>();
        const coordinate = { id: 123 };
        jest.spyOn(coordinateService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ coordinate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(coordinateService.update).toHaveBeenCalledWith(coordinate);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPhotoById', () => {
        it('Should return tracked Photo primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPhotoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
