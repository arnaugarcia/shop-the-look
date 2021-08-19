import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { BillingAddressService } from '../service/billing-address.service';
import { BillingAddress, IBillingAddress } from '../model/billing-address.model';

@Injectable({ providedIn: 'root' })
export class BillingAddressRoutingResolveService implements Resolve<IBillingAddress> {
  constructor(protected service: BillingAddressService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBillingAddress> | Observable<never> {
    return this.service.query().pipe(
      mergeMap((billingAddress: HttpResponse<BillingAddress>) => {
        if (billingAddress.body) {
          return of(billingAddress.body);
        } else {
          return of(new BillingAddress());
        }
      })
    );
  }
}
