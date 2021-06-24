import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SubscriptionPlanDetailComponent } from './subscription-plan-detail.component';

describe('Component Tests', () => {
  describe('SubscriptionPlan Management Detail Component', () => {
    let comp: SubscriptionPlanDetailComponent;
    let fixture: ComponentFixture<SubscriptionPlanDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SubscriptionPlanDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ subscriptionPlan: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SubscriptionPlanDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SubscriptionPlanDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load subscriptionPlan on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.subscriptionPlan).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
