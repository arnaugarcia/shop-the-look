jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { GoogleFeedProductService } from '../service/google-feed-product.service';
import { IGoogleFeedProduct, GoogleFeedProduct } from '../google-feed-product.model';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';

import { GoogleFeedProductUpdateComponent } from './google-feed-product-update.component';

describe('Component Tests', () => {
  describe('GoogleFeedProduct Management Update Component', () => {
    let comp: GoogleFeedProductUpdateComponent;
    let fixture: ComponentFixture<GoogleFeedProductUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let googleFeedProductService: GoogleFeedProductService;
    let companyService: CompanyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GoogleFeedProductUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(GoogleFeedProductUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GoogleFeedProductUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      googleFeedProductService = TestBed.inject(GoogleFeedProductService);
      companyService = TestBed.inject(CompanyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Company query and add missing value', () => {
        const googleFeedProduct: IGoogleFeedProduct = { id: 456 };
        const company: ICompany = { id: 73709 };
        googleFeedProduct.company = company;

        const companyCollection: ICompany[] = [{ id: 94601 }];
        jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
        const additionalCompanies = [company];
        const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
        jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ googleFeedProduct });
        comp.ngOnInit();

        expect(companyService.query).toHaveBeenCalled();
        expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(companyCollection, ...additionalCompanies);
        expect(comp.companiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const googleFeedProduct: IGoogleFeedProduct = { id: 456 };
        const company: ICompany = { id: 40733 };
        googleFeedProduct.company = company;

        activatedRoute.data = of({ googleFeedProduct });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(googleFeedProduct));
        expect(comp.companiesSharedCollection).toContain(company);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<GoogleFeedProduct>>();
        const googleFeedProduct = { id: 123 };
        jest.spyOn(googleFeedProductService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ googleFeedProduct });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: googleFeedProduct }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(googleFeedProductService.update).toHaveBeenCalledWith(googleFeedProduct);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<GoogleFeedProduct>>();
        const googleFeedProduct = new GoogleFeedProduct();
        jest.spyOn(googleFeedProductService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ googleFeedProduct });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: googleFeedProduct }));
        saveSubject.complete();

        // THEN
        expect(googleFeedProductService.create).toHaveBeenCalledWith(googleFeedProduct);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<GoogleFeedProduct>>();
        const googleFeedProduct = { id: 123 };
        jest.spyOn(googleFeedProductService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ googleFeedProduct });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(googleFeedProductService.update).toHaveBeenCalledWith(googleFeedProduct);
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
    });
  });
});
