jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BillingAddressService } from '../service/billing-address.service';
import { IBillingAddress, BillingAddress } from '../billing-address.model';

import { BillingAddressUpdateComponent } from './billing-address-update.component';

describe('Component Tests', () => {
  describe('BillingAddress Management Update Component', () => {
    let comp: BillingAddressUpdateComponent;
    let fixture: ComponentFixture<BillingAddressUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let billingAddressService: BillingAddressService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BillingAddressUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BillingAddressUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BillingAddressUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      billingAddressService = TestBed.inject(BillingAddressService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const billingAddress: IBillingAddress = { id: 456 };

        activatedRoute.data = of({ billingAddress });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(billingAddress));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<BillingAddress>>();
        const billingAddress = { id: 123 };
        jest.spyOn(billingAddressService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ billingAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: billingAddress }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(billingAddressService.update).toHaveBeenCalledWith(billingAddress);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<BillingAddress>>();
        const billingAddress = new BillingAddress();
        jest.spyOn(billingAddressService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ billingAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: billingAddress }));
        saveSubject.complete();

        // THEN
        expect(billingAddressService.create).toHaveBeenCalledWith(billingAddress);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<BillingAddress>>();
        const billingAddress = { id: 123 };
        jest.spyOn(billingAddressService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ billingAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(billingAddressService.update).toHaveBeenCalledWith(billingAddress);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
