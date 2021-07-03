import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BillingAddressService } from '../service/billing-address.service';

import { BillingAddressComponent } from './billing-address.component';

describe('Component Tests', () => {
  describe('BillingAddress Management Component', () => {
    let comp: BillingAddressComponent;
    let fixture: ComponentFixture<BillingAddressComponent>;
    let service: BillingAddressService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BillingAddressComponent],
      })
        .overrideTemplate(BillingAddressComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BillingAddressComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(BillingAddressService);

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
      expect(comp.billingAddresses?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
