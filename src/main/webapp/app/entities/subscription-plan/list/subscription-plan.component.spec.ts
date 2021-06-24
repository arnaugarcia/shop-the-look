import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SubscriptionPlanService } from '../service/subscription-plan.service';

import { SubscriptionPlanComponent } from './subscription-plan.component';

describe('Component Tests', () => {
  describe('SubscriptionPlan Management Component', () => {
    let comp: SubscriptionPlanComponent;
    let fixture: ComponentFixture<SubscriptionPlanComponent>;
    let service: SubscriptionPlanService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SubscriptionPlanComponent],
      })
        .overrideTemplate(SubscriptionPlanComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SubscriptionPlanComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SubscriptionPlanService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.subscriptionPlans?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
