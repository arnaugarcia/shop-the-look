import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BillingAddressDetailComponent } from './billing-address-detail.component';

describe('Component Tests', () => {
  describe('BillingAddress Management Detail Component', () => {
    let comp: BillingAddressDetailComponent;
    let fixture: ComponentFixture<BillingAddressDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BillingAddressDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ billingAddress: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BillingAddressDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BillingAddressDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load billingAddress on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.billingAddress).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
