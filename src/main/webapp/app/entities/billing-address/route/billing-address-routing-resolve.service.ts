import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBillingAddress, BillingAddress } from '../billing-address.model';
import { BillingAddressService } from '../service/billing-address.service';

@Injectable({ providedIn: 'root' })
export class BillingAddressRoutingResolveService implements Resolve<IBillingAddress> {
  constructor(protected service: BillingAddressService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBillingAddress> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((billingAddress: HttpResponse<BillingAddress>) => {
          if (billingAddress.body) {
            return of(billingAddress.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BillingAddress());
  }
}
