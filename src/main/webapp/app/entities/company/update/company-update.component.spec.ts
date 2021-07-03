jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CompanyService } from '../service/company.service';
import { ICompany, Company } from '../company.model';
import { IBillingAddress } from 'app/entities/billing-address/billing-address.model';
import { BillingAddressService } from 'app/entities/billing-address/service/billing-address.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ISubscriptionPlan } from 'app/entities/subscription-plan/subscription-plan.model';
import { SubscriptionPlanService } from 'app/entities/subscription-plan/service/subscription-plan.service';

import { CompanyUpdateComponent } from './company-update.component';

describe('Component Tests', () => {
  describe('Company Management Update Component', () => {
    let comp: CompanyUpdateComponent;
    let fixture: ComponentFixture<CompanyUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let companyService: CompanyService;
    let billingAddressService: BillingAddressService;
    let userService: UserService;
    let subscriptionPlanService: SubscriptionPlanService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CompanyUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CompanyUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CompanyUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      companyService = TestBed.inject(CompanyService);
      billingAddressService = TestBed.inject(BillingAddressService);
      userService = TestBed.inject(UserService);
      subscriptionPlanService = TestBed.inject(SubscriptionPlanService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call billingAddress query and add missing value', () => {
        const company: ICompany = { id: 456 };
        const billingAddress: IBillingAddress = { id: 8056 };
        company.billingAddress = billingAddress;

        const billingAddressCollection: IBillingAddress[] = [{ id: 59126 }];
        jest.spyOn(billingAddressService, 'query').mockReturnValue(of(new HttpResponse({ body: billingAddressCollection })));
        const expectedCollection: IBillingAddress[] = [billingAddress, ...billingAddressCollection];
        jest.spyOn(billingAddressService, 'addBillingAddressToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ company });
        comp.ngOnInit();

        expect(billingAddressService.query).toHaveBeenCalled();
        expect(billingAddressService.addBillingAddressToCollectionIfMissing).toHaveBeenCalledWith(billingAddressCollection, billingAddress);
        expect(comp.billingAddressesCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const company: ICompany = { id: 456 };
        const users: IUser[] = [{ id: 67844 }];
        company.users = users;

        const userCollection: IUser[] = [{ id: 23320 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [...users];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ company });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call SubscriptionPlan query and add missing value', () => {
        const company: ICompany = { id: 456 };
        const subscriptionPlan: ISubscriptionPlan = { id: 86505 };
        company.subscriptionPlan = subscriptionPlan;

        const subscriptionPlanCollection: ISubscriptionPlan[] = [{ id: 67815 }];
        jest.spyOn(subscriptionPlanService, 'query').mockReturnValue(of(new HttpResponse({ body: subscriptionPlanCollection })));
        const additionalSubscriptionPlans = [subscriptionPlan];
        const expectedCollection: ISubscriptionPlan[] = [...additionalSubscriptionPlans, ...subscriptionPlanCollection];
        jest.spyOn(subscriptionPlanService, 'addSubscriptionPlanToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ company });
        comp.ngOnInit();

        expect(subscriptionPlanService.query).toHaveBeenCalled();
        expect(subscriptionPlanService.addSubscriptionPlanToCollectionIfMissing).toHaveBeenCalledWith(
          subscriptionPlanCollection,
          ...additionalSubscriptionPlans
        );
        expect(comp.subscriptionPlansSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const company: ICompany = { id: 456 };
        const billingAddress: IBillingAddress = { id: 12231 };
        company.billingAddress = billingAddress;
        const users: IUser = { id: 40071 };
        company.users = [users];
        const subscriptionPlan: ISubscriptionPlan = { id: 85297 };
        company.subscriptionPlan = subscriptionPlan;

        activatedRoute.data = of({ company });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(company));
        expect(comp.billingAddressesCollection).toContain(billingAddress);
        expect(comp.usersSharedCollection).toContain(users);
        expect(comp.subscriptionPlansSharedCollection).toContain(subscriptionPlan);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Company>>();
        const company = { id: 123 };
        jest.spyOn(companyService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ company });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: company }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(companyService.update).toHaveBeenCalledWith(company);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Company>>();
        const company = new Company();
        jest.spyOn(companyService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ company });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: company }));
        saveSubject.complete();

        // THEN
        expect(companyService.create).toHaveBeenCalledWith(company);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Company>>();
        const company = { id: 123 };
        jest.spyOn(companyService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ company });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(companyService.update).toHaveBeenCalledWith(company);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackBillingAddressById', () => {
        it('Should return tracked BillingAddress primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBillingAddressById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackSubscriptionPlanById', () => {
        it('Should return tracked SubscriptionPlan primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSubscriptionPlanById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedUser', () => {
        it('Should return option if no User is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedUser(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected User for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedUser(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this User is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedUser(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
