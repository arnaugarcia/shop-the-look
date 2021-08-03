import { IProduct, Product } from '../../models/product.model';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { ICoordinate } from 'app/entities/coordinate/coordinate.model';
import { CoordinateService } from 'app/entities/coordinate/service/coordinate.service';

import { ProductUpdateComponent } from './product-update.component';
import { ProductService } from '../../services/product.service';

jest.mock('@angular/router');

describe('Component Tests', () => {
  describe('Product Management Update Component', () => {
    let comp: ProductUpdateComponent;
    let fixture: ComponentFixture<ProductUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let productService: ProductService;
    let companyService: CompanyService;
    let coordinateService: CoordinateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProductUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProductUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProductUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      productService = TestBed.inject(ProductService);
      companyService = TestBed.inject(CompanyService);
      coordinateService = TestBed.inject(CoordinateService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Company query and add missing value', () => {
        const product: IProduct = { id: 456 };
        const company: ICompany = { id: 3248 };
        product.company = company;

        const companyCollection: ICompany[] = [{ id: 68340 }];
        jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
        const additionalCompanies = [company];
        const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
        jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ product });
        comp.ngOnInit();

        expect(companyService.query).toHaveBeenCalled();
        expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(companyCollection, ...additionalCompanies);
        expect(comp.companiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Coordinate query and add missing value', () => {
        const product: IProduct = { id: 456 };
        const coordinate: ICoordinate = { id: 97998 };
        product.coordinate = coordinate;

        const coordinateCollection: ICoordinate[] = [{ id: 30857 }];
        jest.spyOn(coordinateService, 'query').mockReturnValue(of(new HttpResponse({ body: coordinateCollection })));
        const additionalCoordinates = [coordinate];
        const expectedCollection: ICoordinate[] = [...additionalCoordinates, ...coordinateCollection];
        jest.spyOn(coordinateService, 'addCoordinateToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ product });
        comp.ngOnInit();

        expect(coordinateService.query).toHaveBeenCalled();
        expect(coordinateService.addCoordinateToCollectionIfMissing).toHaveBeenCalledWith(coordinateCollection, ...additionalCoordinates);
        expect(comp.coordinatesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const product: IProduct = { id: 456 };
        const company: ICompany = { id: 55439 };
        product.company = company;
        const coordinate: ICoordinate = { id: 99840 };
        product.coordinate = coordinate;

        activatedRoute.data = of({ product });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(product));
        expect(comp.companiesSharedCollection).toContain(company);
        expect(comp.coordinatesSharedCollection).toContain(coordinate);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Product>>();
        const product = { id: 123 };
        jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ product });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: product }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(productService.update).toHaveBeenCalledWith(product);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Product>>();
        const product = new Product();
        jest.spyOn(productService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ product });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: product }));
        saveSubject.complete();

        // THEN
        expect(productService.create).toHaveBeenCalledWith(product);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Product>>();
        const product = { id: 123 };
        jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ product });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(productService.update).toHaveBeenCalledWith(product);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCompanyById', () => {
        it('Should return tracked Company primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCompanyById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCoordinateById', () => {
        it('Should return tracked Coordinate primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCoordinateById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
