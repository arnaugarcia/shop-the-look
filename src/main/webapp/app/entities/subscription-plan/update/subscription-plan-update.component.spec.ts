jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SubscriptionPlanService } from '../service/subscription-plan.service';
import { ISubscriptionPlan, SubscriptionPlan } from '../subscription-plan.model';

import { SubscriptionPlanUpdateComponent } from './subscription-plan-update.component';

describe('Component Tests', () => {
  describe('SubscriptionPlan Management Update Component', () => {
    let comp: SubscriptionPlanUpdateComponent;
    let fixture: ComponentFixture<SubscriptionPlanUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let subscriptionPlanService: SubscriptionPlanService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SubscriptionPlanUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SubscriptionPlanUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SubscriptionPlanUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      subscriptionPlanService = TestBed.inject(SubscriptionPlanService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const subscriptionPlan: ISubscriptionPlan = { id: 456 };

        activatedRoute.data = of({ subscriptionPlan });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(subscriptionPlan));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SubscriptionPlan>>();
        const subscriptionPlan = { id: 123 };
        jest.spyOn(subscriptionPlanService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ subscriptionPlan });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: subscriptionPlan }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(subscriptionPlanService.update).toHaveBeenCalledWith(subscriptionPlan);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SubscriptionPlan>>();
        const subscriptionPlan = new SubscriptionPlan();
        jest.spyOn(subscriptionPlanService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ subscriptionPlan });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: subscriptionPlan }));
        saveSubject.complete();

        // THEN
        expect(subscriptionPlanService.create).toHaveBeenCalledWith(subscriptionPlan);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SubscriptionPlan>>();
        const subscriptionPlan = { id: 123 };
        jest.spyOn(subscriptionPlanService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ subscriptionPlan });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(subscriptionPlanService.update).toHaveBeenCalledWith(subscriptionPlan);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
